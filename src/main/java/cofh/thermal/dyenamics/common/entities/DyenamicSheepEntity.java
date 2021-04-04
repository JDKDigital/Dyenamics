package cofh.thermal.dyenamics.common.entities;

import com.google.common.collect.Maps;

import cofh.thermal.dyenamics.core.init.Init;
import cofh.thermal.dyenamics.core.util.DyenamicDyeColor;
import cofh.thermal.dyenamics.common.items.DyenamicDyeItem;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IShearable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DyenamicSheepEntity extends AnimalEntity implements IShearable, net.minecraftforge.common.IForgeShearable {
   private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(DyenamicSheepEntity.class, DataSerializers.BYTE);
   private static final Map<DyenamicDyeColor, IItemProvider> WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyenamicDyeColor.class), (p_203402_0_) -> {
      p_203402_0_.put(DyenamicDyeColor.PEACH, Blocks.WHITE_WOOL); 
      //Add more and fix wool!!!
   });
   /** Map from EnumDyenamicDyeColor to RGB values for passage to GlStateManager.color() */
   private static final Map<DyenamicDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(Arrays.stream(DyenamicDyeColor.values()).collect(Collectors.toMap((DyenamicDyeColor p_200204_0_) -> {
      return p_200204_0_;
   }, DyenamicSheepEntity::createSheepColor)));
   private int sheepTimer;
   private EatGrassGoal eatGrassGoal;

   private static float[] createSheepColor(DyenamicDyeColor dyeColorIn) {
       float[] afloat = dyeColorIn.getColorComponentValues();
       float f = 0.75F;
       return new float[]{afloat[0] * f, afloat[1] * f, afloat[2] * f};
   }

   @OnlyIn(Dist.CLIENT)
   public static float[] getDyeRgb(DyenamicDyeColor dyeColor) {
      return DYE_TO_RGB.get(dyeColor);
   }

   public DyenamicSheepEntity(EntityType<? extends DyenamicSheepEntity> type, World worldIn) {
      super(type, worldIn);
   }

   protected void registerGoals() {
      this.eatGrassGoal = new EatGrassGoal(this);
      this.goalSelector.addGoal(0, new SwimGoal(this));
      this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.fromItems(Items.WHEAT), false));
      this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
      this.goalSelector.addGoal(5, this.eatGrassGoal);
      this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
      this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
      this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
   }

   protected void updateAITasks() {
      this.sheepTimer = this.eatGrassGoal.getEatingGrassTimer();
      super.updateAITasks();
   }

   /**
    * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
    * use this to react to sunlight and start to burn.
    */
   public void livingTick() {
      if (this.world.isRemote) {
         this.sheepTimer = Math.max(0, this.sheepTimer - 1);
      }

      super.livingTick();
   }

   public static AttributeModifierMap.MutableAttribute func_234225_eI_() {
      return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 8.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.23F);
   }

   protected void registerData() {
      super.registerData();
      this.dataManager.register(DYE_COLOR, (byte)0);
   }

   /*register own loot tables
   public ResourceLocation getLootTable() {
      if (this.getSheared()) {
         return this.getType().getLootTable();
      } else {
         switch(this.getFleeceColor()) {
         case WHITE:
         default:
            return LootTables.ENTITIES_SHEEP_WHITE;
         case ORANGE:
            return LootTables.ENTITIES_SHEEP_ORANGE;
         case MAGENTA:
            return LootTables.ENTITIES_SHEEP_MAGENTA;
         case LIGHT_BLUE:
            return LootTables.ENTITIES_SHEEP_LIGHT_BLUE;
         case YELLOW:
            return LootTables.ENTITIES_SHEEP_YELLOW;
         case LIME:
            return LootTables.ENTITIES_SHEEP_LIME;
         case PINK:
            return LootTables.ENTITIES_SHEEP_PINK;
         case GRAY:
            return LootTables.ENTITIES_SHEEP_GRAY;
         case LIGHT_GRAY:
            return LootTables.ENTITIES_SHEEP_LIGHT_GRAY;
         case CYAN:
            return LootTables.ENTITIES_SHEEP_CYAN;
         case PURPLE:
            return LootTables.ENTITIES_SHEEP_PURPLE;
         case BLUE:
            return LootTables.ENTITIES_SHEEP_BLUE;
         case BROWN:
            return LootTables.ENTITIES_SHEEP_BROWN;
         case GREEN:
            return LootTables.ENTITIES_SHEEP_GREEN;
         case RED:
            return LootTables.ENTITIES_SHEEP_RED;
         case BLACK:
            return LootTables.ENTITIES_SHEEP_BLACK;
         }
      }
   }
    */
   /**
    * Handler for {@link World#setEntityState}
    */
   @OnlyIn(Dist.CLIENT)
   public void handleStatusUpdate(byte id) {
      if (id == 10) {
         this.sheepTimer = 40;
      } else {
         super.handleStatusUpdate(id);
      }

   }

   @OnlyIn(Dist.CLIENT)
   public float getHeadRotationPointY(float p_70894_1_) {
      if (this.sheepTimer <= 0) {
         return 0.0F;
      } else if (this.sheepTimer >= 4 && this.sheepTimer <= 36) {
         return 1.0F;
      } else {
         return this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0F : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0F;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public float getHeadRotationAngleX(float p_70890_1_) {
      if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
         float f = ((float)(this.sheepTimer - 4) - p_70890_1_) / 32.0F;
         return ((float)Math.PI / 5F) + 0.21991149F * MathHelper.sin(f * 28.7F);
      } else {
         return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch * ((float)Math.PI / 180F);
      }
   }

   public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
      ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
      if (false && itemstack.getItem() == Items.SHEARS) { //Forge: Moved to onSheared
         if (!this.world.isRemote && this.isShearable()) {
            this.shear(SoundCategory.PLAYERS);
            itemstack.damageItem(1, p_230254_1_, (p_213613_1_) -> {
               p_213613_1_.sendBreakAnimation(p_230254_2_);
            });
            return ActionResultType.SUCCESS;
         } else {
            return ActionResultType.CONSUME;
         }
      } else {
         return super.func_230254_b_(p_230254_1_, p_230254_2_);
      }
   }

   public void shear(SoundCategory category) {
      this.world.playMovingSound((PlayerEntity)null, this, SoundEvents.ENTITY_SHEEP_SHEAR, category, 1.0F, 1.0F);
      this.setSheared(true);
      int i = 1 + this.rand.nextInt(3);

      for(int j = 0; j < i; ++j) {
         ItemEntity itementity = this.entityDropItem(WOOL_BY_COLOR.get(this.getFleeceColor()), 1);
         if (itementity != null) {
            itementity.setMotion(itementity.getMotion().add((double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F), (double)(this.rand.nextFloat() * 0.05F), (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F)));
         }
      }

   }

   public boolean isShearable() {
      return this.isAlive() && !this.getSheared() && !this.isChild();
   }

   public void writeAdditional(CompoundNBT compound) {
      super.writeAdditional(compound);
      compound.putBoolean("Sheared", this.getSheared());
      compound.putByte("Color", (byte)this.getFleeceColor().getId());
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditional(CompoundNBT compound) {
      super.readAdditional(compound);
      this.setSheared(compound.getBoolean("Sheared"));
      this.setFleeceColor(DyenamicDyeColor.byId(compound.getByte("Color")));
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SHEEP_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.ENTITY_SHEEP_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SHEEP_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
   }

   /**
    * Gets the wool color of this sheep.
    */
   public DyenamicDyeColor getFleeceColor() {
      return DyenamicDyeColor.byId(this.dataManager.get(DYE_COLOR) & 15);
   }

   /**
    * Sets the wool color of this sheep
    */
   public void setFleeceColor(DyenamicDyeColor color) {
      byte b0 = this.dataManager.get(DYE_COLOR);
      this.dataManager.set(DYE_COLOR, (byte)(b0 & 240 | color.getId() & 15));
   }

   /**
    * returns true if a sheeps wool has been sheared
    */
   public boolean getSheared() {
      return (this.dataManager.get(DYE_COLOR) & 16) != 0;
   }

   /**
    * make a sheep sheared if set to true
    */
   public void setSheared(boolean sheared) {
      byte b0 = this.dataManager.get(DYE_COLOR);
      if (sheared) {
         this.dataManager.set(DYE_COLOR, (byte)(b0 | 16));
      } else {
         this.dataManager.set(DYE_COLOR, (byte)(b0 & -17));
      }

   }

   public DyenamicSheepEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
      DyenamicSheepEntity sheepentity = (DyenamicSheepEntity)p_241840_2_;
      DyenamicSheepEntity sheepentity1 = (DyenamicSheepEntity) Init.DYEABLE_ENTITIES.get("sheep").get().create(p_241840_1_); 
      sheepentity1.setFleeceColor(this.getDyenamicDyeColorMixFromParents(this, sheepentity));
      return sheepentity1;
   }

   /**
    * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This function
    * is used in the AIEatGrass)
    */
   public void eatGrassBonus() {
      this.setSheared(false);
      if (this.isChild()) {
         this.addGrowth(60);
      }

   }

   @Nullable
   public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
      this.setFleeceColor(DyenamicDyeColor.PEACH);
      return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   /**
    * Attempts to mix both parent sheep to come up with a mixed dye color.
    */
   private DyenamicDyeColor getDyenamicDyeColorMixFromParents(AnimalEntity father, AnimalEntity mother) {
      DyenamicDyeColor dyecolor = ((DyenamicSheepEntity)father).getFleeceColor();
      DyenamicDyeColor dyecolor1 = ((DyenamicSheepEntity)mother).getFleeceColor();
      CraftingInventory craftinginventory = createDyenamicDyeColorCraftingInventory(dyecolor, dyecolor1);
      return this.world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftinginventory, this.world).map((p_213614_1_) -> {
         return p_213614_1_.getCraftingResult(craftinginventory);
      }).map(ItemStack::getItem).filter(DyenamicDyeItem.class::isInstance).map(DyenamicDyeItem.class::cast).map(DyenamicDyeItem::getDyeColor).orElseGet(() -> {
         return this.world.rand.nextBoolean() ? dyecolor : dyecolor1;
      });
   }

   private static CraftingInventory createDyenamicDyeColorCraftingInventory(DyenamicDyeColor color, DyenamicDyeColor color1) {
      CraftingInventory craftinginventory = new CraftingInventory(new Container((ContainerType)null, -1) {
         /**
          * Determines whether supplied player can use this container
          */
         public boolean canInteractWith(PlayerEntity playerIn) {
            return false;
         }
      }, 2, 1);
      craftinginventory.setInventorySlotContents(0, new ItemStack(DyenamicDyeItem.getItem(color)));
      craftinginventory.setInventorySlotContents(1, new ItemStack(DyenamicDyeItem.getItem(color1)));
      return craftinginventory;
   }

   protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
      return 0.95F * sizeIn.height;
   }

   @Override
   public boolean isShearable(@javax.annotation.Nonnull ItemStack item, World world, BlockPos pos) {
      return isShearable();
   }

   @javax.annotation.Nonnull
   @Override
   public java.util.List<ItemStack> onSheared(@Nullable PlayerEntity player, @javax.annotation.Nonnull ItemStack item, World world, BlockPos pos, int fortune) {
      world.playMovingSound(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, player == null ? SoundCategory.BLOCKS : SoundCategory.PLAYERS, 1.0F, 1.0F);
      if (!world.isRemote) {
         this.setSheared(true);
         int i = 1 + this.rand.nextInt(3);

         java.util.List<ItemStack> items = new java.util.ArrayList<>();
         for (int j = 0; j < i; ++j) {
            items.add(new ItemStack(WOOL_BY_COLOR.get(this.getFleeceColor())));
         }
         return items;
      }
      return java.util.Collections.emptyList();
   }
}

