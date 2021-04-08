package dyenamics.common.entities;

import com.google.common.collect.Maps;

import dyenamics.core.init.BlockInit;
import dyenamics.core.init.EntityInit;
import dyenamics.core.util.DyenamicDyeColor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DyenamicSheepEntity extends SheepEntity {
   protected static final Map<DyenamicDyeColor, IItemProvider> WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyenamicDyeColor.class), (p_203402_0_) -> {
	   for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
		   p_203402_0_.put(color, BlockInit.DYED_BLOCKS.get(color.getString()).get("wool").get());
	   }
   });
   /** Map from EnumDyenamicDyeColor to RGB values for passage to GlStateManager.color() */
   protected static final Map<DyenamicDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(Arrays.stream(DyenamicDyeColor.values()).collect(Collectors.toMap((DyenamicDyeColor color) -> {
      return color;
   }, DyenamicSheepEntity::createSheepColor)));

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
   
   /*
   public DyenamicSheepEntity(EntityType<? extends DyenamicSheepEntity> type, World worldIn, DyenamicDyeColor color) {
	   super(type, worldIn);
	   this.setFleeceColor(color);
   }
	*/
   
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
		   SheepEntity sheep = new SheepEntity(EntityType.SHEEP, world);
		   sheep.setFleeceColor(color);
		   sheep.copyLocationAndAnglesFrom(oldSheep);
		   sheep.setChild(oldSheep.isChild());
		   world.addEntity(sheep);
		   oldSheep.remove();
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
		   oldSheep.remove();
		   return sheep;
	   }
	   return null;
   }

   public ResourceLocation getLootTable() {
      if (this.getSheared()) {
         return this.getType().getLootTable();
      } else {
         return EntityInit.SHEEP_LOOT.get(this.getDyenamicFleeceColor().getTranslationKey());
      }
   }
   
   public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
	      ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
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
	         return super.func_230254_b_(p_230254_1_, p_230254_2_);
	      }
	   }

   public void shear(SoundCategory category) {
      this.world.playMovingSound((PlayerEntity)null, this, SoundEvents.ENTITY_SHEEP_SHEAR, category, 1.0F, 1.0F);
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

   public void writeAdditional(CompoundNBT compound) {
      super.writeAdditional(compound);
      compound.putBoolean("Sheared", this.getSheared());
      compound.putByte("Color", (byte)this.getDyenamicFleeceColor().getId()); 
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditional(CompoundNBT compound) {
      super.readAdditional(compound);
      this.setSheared(compound.getBoolean("Sheared"));
      this.setFleeceColor(DyenamicDyeColor.byId(compound.getByte("Color"))); 
   }
   
   public DyenamicDyeColor getDyenamicFleeceColor() {
      return DyenamicDyeColor.byId(this.dataManager.get(DYE_COLOR) & 127); 
   }
   
   /**
    * Sets the wool color of this sheep
    */
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
   public boolean getSheared() {
      return this.dataManager.get(DYE_COLOR) < 0;
   }
   

   /**
    * Make a sheep sheared if set to true (positive = unsheared, negative = sheared)
    */
   public void setSheared(boolean sheared) {
      byte b0 = this.dataManager.get(DYE_COLOR);
      if (sheared) {
         this.dataManager.set(DYE_COLOR, (byte)(b0 | -128));
      } else {
         this.dataManager.set(DYE_COLOR, (byte)(b0 & 127));
      }

   }

   public DyenamicSheepEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
      DyenamicSheepEntity sheepentity = (DyenamicSheepEntity)p_241840_2_;
      DyenamicSheepEntity sheepentity1 = (DyenamicSheepEntity) EntityInit.SHEEP.get().create(p_241840_1_); 
      sheepentity1.setFleeceColor(this.getDyeColorMixFromParents(this, sheepentity));
      return sheepentity1;
   }

   @Nullable
   public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
	  ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	  this.setFleeceColor(DyenamicDyeColor.PEACH);
      return data;
   }

   /**
    * Attempts to mix both parent sheep to come up with a mixed dye color.
    */
   //TODO: accept both vanilla and dyenamic
   protected DyenamicDyeColor getDyeColorMixFromParents(SheepEntity father, DyenamicSheepEntity mother) {
	   return this.world.rand.nextBoolean() ? DyenamicDyeColor.byId(father.getFleeceColor().getId()) : ((DyenamicSheepEntity)mother).getDyenamicFleeceColor(); 
   }
   
   protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheepEntity mother, SheepEntity father) {
	   return this.world.rand.nextBoolean() ? DyenamicDyeColor.byId(father.getFleeceColor().getId()) : ((DyenamicSheepEntity)mother).getDyenamicFleeceColor(); 
   }
   
   protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheepEntity father, DyenamicSheepEntity mother) {
	   return this.world.rand.nextBoolean() ? ((DyenamicSheepEntity)father).getDyenamicFleeceColor() : ((DyenamicSheepEntity)mother).getDyenamicFleeceColor();
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
}

