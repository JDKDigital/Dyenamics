package cofh.dyenamics.common.entities;

import cofh.dyenamics.core.init.BlockInit;
import cofh.dyenamics.core.init.EntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import com.google.common.collect.Maps;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class DyenamicSheepEntity extends SheepEntity {
   protected static final Map<DyenamicDyeColor, IItemProvider> WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyenamicDyeColor.class), (map) -> {
	   for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
		   map.put(color, BlockInit.DYED_BLOCKS.get(color.getString()).get("wool").get());
	   }
   });
   /** Map from EnumDyenamicDyeColor to RGB values for passage to GlStateManager.color() */
   protected static final Map<DyenamicDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(Arrays.stream(DyenamicDyeColor.values()).collect(Collectors.toMap((DyenamicDyeColor color) -> color, DyenamicSheepEntity::createSheepColor)));

   protected static float[] createSheepColor(DyenamicDyeColor dyeColorIn) {
	   if (dyeColorIn == DyenamicDyeColor.WHITE) {
	         return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
	      } else {
	         float[] afloat = dyeColorIn.getColorComponentValues();
	         float f = 0.75F;
	         return new float[]{afloat[0] * f, afloat[1] * f, afloat[2] * f};
	      }
   }

   @OnlyIn(Dist.CLIENT)
   public static float[] getDyeRgb(DyenamicDyeColor dyeColor) {
      return DYE_TO_RGB.get(dyeColor);
   }

   public DyenamicSheepEntity(EntityType<? extends DyenamicSheepEntity> type, World worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void registerGoals() {
	  super.registerGoals();
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D, SheepEntity.class));
	}

   public static AttributeModifierMap.MutableAttribute registerAttributes() {
      return SheepEntity.func_234225_eI_();
   }
   
   /*
    * Methods for converting between vanilla and dyenamics sheeps. Deletes the old sheep and spawns a new one.
    */
   public static SheepEntity convertToVanilla(DyenamicSheepEntity oldSheep) {
	   return convertToVanilla(oldSheep, DyeColor.WHITE);
   }
   
   public static SheepEntity convertToVanilla(DyenamicSheepEntity oldSheep, DyeColor color) {
	   World world = oldSheep.world;
	   if (!world.isRemote) {
           oldSheep.remove();
		   SheepEntity sheep = new SheepEntity(EntityType.SHEEP, world);
		   sheep.setFleeceColor(color);
		   sheep.copyLocationAndAnglesFrom(oldSheep);
		   sheep.setChild(oldSheep.isChild());
		   world.addEntity(sheep);
		   return sheep;
	   }
	   return null;
   }
   
   public static DyenamicSheepEntity convertToDyenamics(SheepEntity oldSheep) {
	   return convertToDyenamics(oldSheep, DyenamicDyeColor.PEACH);
   }
   
   public static DyenamicSheepEntity convertToDyenamics(SheepEntity oldSheep, DyenamicDyeColor color) {
	   World world = oldSheep.world;
	   if (!world.isRemote) {
		   oldSheep.remove();
		   DyenamicSheepEntity sheep = new DyenamicSheepEntity(EntityInit.SHEEP.get(), world);
		   sheep.setFleeceColor(color);
		   sheep.copyLocationAndAnglesFrom(oldSheep);
		   sheep.setChild(oldSheep.isChild());
		   world.addEntity(sheep);
		   return sheep;
	   }
	   return null;
   }

   @Override
   public ResourceLocation getLootTable() {
      if (this.getSheared()) {
         return this.getType().getLootTable();
      } else {
         return EntityInit.SHEEP_LOOT.get(this.getDyenamicFleeceColor().getTranslationKey());
      }
   }

   @Override
   public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
      ItemStack itemstack = player.getHeldItem(hand);
      if (itemstack.getItem() instanceof DyeItem) { 
         if (!this.world.isRemote) {
            DyenamicSheepEntity.convertToVanilla(this, ((DyeItem) itemstack.getItem()).getDyeColor());
		  	itemstack.shrink(1);
            return ActionResultType.SUCCESS;
         } else {
            return ActionResultType.CONSUME;
         }
      }
      else if (itemstack.getItem().equals(Items.SHEEP_SPAWN_EGG)) {
    	  if (!this.world.isRemote) {
    		  	DyenamicSheepEntity sheep = new DyenamicSheepEntity(EntityInit.SHEEP.get(), this.world);
    		  	sheep.copyLocationAndAnglesFrom(this);
    		  	sheep.setChild(true);
    		  	sheep.setFleeceColor(this.getDyenamicFleeceColor());
    		  	this.world.addEntity(sheep);
    		  	itemstack.shrink(1);
	            return ActionResultType.SUCCESS;
	      } else {
	         return ActionResultType.CONSUME;
	      }
      }
      else {
         return super.func_230254_b_(player, hand);
      }
   }

   @Override
   public void shear(SoundCategory category) {
      this.world.playMovingSound(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, category, 1.0F, 1.0F);
      this.setSheared(true);
      int i = 1 + this.rand.nextInt(3);
      
	  DyenamicDyeColor color = this.getDyenamicFleeceColor();

      for(int j = 0; j < i; ++j) {
    	  ItemEntity itementity;
     	 if (color.getId() > 15) {
             itementity = this.entityDropItem(WOOL_BY_COLOR.get(this.getDyenamicFleeceColor()), 1);
     	 }
     	 else {
             itementity = this.entityDropItem(SheepEntity.WOOL_BY_COLOR.get(color.getVanillaColor()), 1);
     	 }
         if (itementity != null) {
            itementity.setMotion(itementity.getMotion().add((double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F), (double)(this.rand.nextFloat() * 0.05F), (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F)));
         }
      }
   }

   @Override
   public void writeAdditional(CompoundNBT compound) {
      super.writeAdditional(compound);
      compound.putByte("Color", (byte)this.getDyenamicFleeceColor().getId()); 
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   @Override
   public void readAdditional(CompoundNBT compound) {
      super.readAdditional(compound);
      this.setFleeceColor(DyenamicDyeColor.byId(compound.getByte("Color"))); 
   }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
   
   public DyenamicDyeColor getDyenamicFleeceColor() {
      return DyenamicDyeColor.byId(this.dataManager.get(DYE_COLOR) & 127); 
   }
   
   /**
    * Sets the wool color of this sheep
    */
   @Override
   public void setFleeceColor(DyeColor color) {
      byte b0 = this.dataManager.get(DYE_COLOR);
      this.dataManager.set(DYE_COLOR, (byte)(b0 & -128 | color.getId() & 15));
   }
   
   public void setFleeceColor(DyenamicDyeColor color) {
      byte b0 = this.dataManager.get(DYE_COLOR);
      this.dataManager.set(DYE_COLOR, (byte)(b0 & -128 | (color.getId() & 127)));
   }

   /**
    * returns true if a sheep's wool has been sheared
    */
   @Override
   public boolean getSheared() {
      return this.dataManager.get(DYE_COLOR) < 0;
   }

   /**
    * Make a sheep sheared if set to true (positive = unsheared, negative = sheared)
    */
   @Override
   public void setSheared(boolean sheared) {
      byte b0 = this.dataManager.get(DYE_COLOR);
      if (sheared) {
         this.dataManager.set(DYE_COLOR, (byte)(b0 | -128));
      } else {
         this.dataManager.set(DYE_COLOR, (byte)(b0 & 127));
      }
   }

   @Override
   public SheepEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
	   if (mate instanceof DyenamicSheepEntity) {
		   DyenamicSheepEntity parent = (DyenamicSheepEntity)mate;
		   DyenamicSheepEntity child = (DyenamicSheepEntity) EntityInit.SHEEP.get().create(world); 
		   child.setFleeceColor(this.getDyeColorMixFromParents(this, parent));
		   return child;
	   }
	   else {
		   SheepEntity parent = (SheepEntity)mate;
		   DyenamicDyeColor color = this.getDyeColorMixFromParents(this, parent);
		   if (color.getId() < 16) {
			   SheepEntity child = (SheepEntity) EntityType.SHEEP.create(world); 
			   child.setFleeceColor(color.getAnalogue());
			   return child;
		   }
		   else {
			   DyenamicSheepEntity child = (DyenamicSheepEntity) EntityInit.SHEEP.get().create(world); 
			   child.setFleeceColor(color);
			   return child;
		   }
		   
	   }
   }

   @Nullable
   public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
	  ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	  this.setFleeceColor(DyenamicDyeColor.PEACH);
      return data;
   }

   @Override
   public boolean canMateWith(AnimalEntity otherAnimal) {
      if (otherAnimal == this) {
         return false;
      } else if (otherAnimal instanceof SheepEntity) {
    	  return this.isInLove() && otherAnimal.isInLove();
      }
      else if (otherAnimal.getClass() != this.getClass()) {
         return false;
      } else {
         return this.isInLove() && otherAnimal.isInLove();
      }
   }
   
   /**
    * Attempts to mix both parent sheep to come up with a mixed dye color.
    */
   protected DyenamicDyeColor getDyeColorMixFromParents(SheepEntity father, DyenamicSheepEntity mother) {
	   return this.world.rand.nextBoolean() ? DyenamicDyeColor.byId(father.getFleeceColor().getId()) : mother.getDyenamicFleeceColor();
   }
   
   protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheepEntity mother, SheepEntity father) {
	   return this.world.rand.nextBoolean() ? DyenamicDyeColor.byId(father.getFleeceColor().getId()) : mother.getDyenamicFleeceColor();
   }
   
   protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheepEntity father, DyenamicSheepEntity mother) {
	   return this.world.rand.nextBoolean() ? father.getDyenamicFleeceColor() : mother.getDyenamicFleeceColor();
   }

   @javax.annotation.Nonnull
   @Override
   public java.util.List<ItemStack> onSheared(@Nullable PlayerEntity player, @javax.annotation.Nonnull ItemStack item, World world, BlockPos pos, int fortune) {
      world.playMovingSound(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, player == null ? SoundCategory.BLOCKS : SoundCategory.PLAYERS, 1.0F, 1.0F);
      if (!world.isRemote) {
         this.setSheared(true);
         int i = 1 + this.rand.nextInt(3);
         
         DyenamicDyeColor color = this.getDyenamicFleeceColor();
         ItemStack stack;
    	 if (color.getId() > 15) {
    		 stack = new ItemStack(WOOL_BY_COLOR.get(color));
    	 }
    	 else {
    		 stack = new ItemStack(SheepEntity.WOOL_BY_COLOR.get(color.getVanillaColor()));
    	 }
         java.util.List<ItemStack> items = new java.util.ArrayList<>();
         for (int j = 0; j < i; ++j) {
        	 items.add(stack);
         }
         return items;
      }
      return java.util.Collections.emptyList();
   }
   
   @Override
   public ItemStack getPickedResult(RayTraceResult target) {
       return new ItemStack(SpawnEggItem.getEgg(EntityType.SHEEP));
   }
}

