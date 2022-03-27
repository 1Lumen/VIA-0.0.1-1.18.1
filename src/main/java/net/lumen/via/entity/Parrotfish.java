package net.lumen.via.entity;

import net.lumen.via.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralBlock;
import net.minecraft.block.CoralFanBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Parrotfish extends FishEntity implements IAnimatable {
    public int moreCoralTicks;
    private AnimationFactory factory = new AnimationFactory(this);

    public Parrotfish(EntityType<? extends Parrotfish> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new FleeEntityGoal<PlayerEntity>(this, PlayerEntity.class, 8.0f, 1.6, 1.4, EntityPredicates.EXCEPT_SPECTATOR::test));
        this.goalSelector.add(4, new SwimToRandomPlaceGoal(this));
        this.goalSelector.add(5, new EatCoralGoal(this));
    }

    @Override
    protected void mobTick() {
        if (this.moreCoralTicks > 0) {
            this.moreCoralTicks -= this.random.nextInt(3);
            if (this.moreCoralTicks < 0) {
                this.moreCoralTicks = 0;
            }
        }
        super.mobTick();
    }

    boolean wantsCorals() {
        return this.moreCoralTicks == 0;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.isTouchingWater() && this.onGround && this.verticalCollision) {
            this.setVelocity(this.getVelocity().add((this.random.nextFloat() * 2.0f - 1.0f) * 0.05f, 0.4f, (this.random.nextFloat() * 2.0f - 1.0f) * 0.05f));
            this.onGround = false;
            this.velocityDirty = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public ItemEntity onEatingCoral() {
        return this.dropItem(Items.SAND, 0);
    }

    public static DefaultAttributeContainer.Builder createParrotfishEntityAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_TROPICAL_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_TROPICAL_FISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_TROPICAL_FISH_DEATH;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ItemRegistry.PARROTFISH_BUCKET);
    }

    protected boolean hasSelfControl() {
        return true;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!isTouchingWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("flop", true));
        } else event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<Parrotfish>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class EatCoralGoal extends MoveToTargetPosGoal {
        private final Parrotfish fish;
        private boolean hasTarget;
        private boolean wantsCorals;

        public EatCoralGoal(Parrotfish fish) {
            super(fish, 1.0f, 20);
            this.fish = fish;
        }

        @Override
        public boolean canStart() {
            if (this.cooldown <= 0) {
                if (!this.fish.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    return false;
                }
                this.hasTarget = false;
                this.wantsCorals = this.fish.wantsCorals();
                this.wantsCorals = true;
            }
            return super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return this.hasTarget && super.shouldContinue();
        }

        @Override
        public void tick() {
            super.tick();
            this.fish.getLookControl().lookAt((double) this.targetPos.getX() + 0.5, this.targetPos.getY() + 0.5, (double) this.targetPos.getZ() + 0.5, 10.0f, this.fish.getMaxLookPitchChange());
            if (this.hasReached()) {
                World world = this.fish.world;
                BlockPos blockPos = this.targetPos.up();
                BlockState blockState = world.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (this.hasTarget && block instanceof CoralFanBlock || block instanceof CoralBlock && this.hasTarget) {
                    world.breakBlock(blockPos, false);
                    world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
                    this.fish.onEatingCoral();
                    this.fish.moreCoralTicks = 40;
                }
                this.hasTarget = false;
                this.cooldown = 10;
            }
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockState blockState = world.getBlockState(pos.up());
            if (blockState.getBlock() instanceof CoralFanBlock && this.wantsCorals && !this.hasTarget || blockState.getBlock() instanceof CoralBlock && this.wantsCorals && !this.hasTarget) {
                this.hasTarget = true;
                return true;
            }
            return false;
        }
    }

    static class SwimToRandomPlaceGoal
            extends SwimAroundGoal {
        private final Parrotfish fish;

        public SwimToRandomPlaceGoal(Parrotfish fish) {
            super(fish, 1.0, 40);
            this.fish = fish;
        }

        @Override
        public boolean canStart() {
            return this.fish.hasSelfControl() && super.canStart();
        }
    }
}
