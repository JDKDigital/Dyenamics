import os
from PIL import Image
from PIL.ImageColor import getcolor, getrgb
from PIL.ImageOps import grayscale


modid = "thermal_dyenamics"
folder = f"resources/assets/{modid}/textures/block"

colors = {"peach": "#BF9873", 
	"aquamarine": "#2C7D7F", 
	"fluorescent": "#EEE9B6", 
	"mint": "#89e8b8", 
	"maroon": "#910000", 
	"bubblegum": "#f771c6", 
	"lavender": "#DD99FF", 
	"persimmon": "#d24119", 
	"cherenkov": "#0198CF"
}

blocks = ["terracotta", "concrete", "concrete_powder", "wool", "stained_glass", "stained_glass_pane_top"]
baseTextures = {}
for block in blocks:
	baseTextures[block] = Image.open(f"textures/{block}.png")

def colorImage(image, color='#ffffff'):
	image.load()
	split = image.split()
	
	mult = tuple(map(lambda i: i/255, getrgb(color)))
	for i in range(3):
		split[i].paste(split[i].point(lambda band: band * mult[i]))
	
	return Image.merge(image.mode, split)

#File Layout
def genFolders():
	os.makedirs(folder, exist_ok=True)

#Textures
def genTextures():
	for color in colors:
		for texture in baseTextures:
			if not (color in {"peach", "aquamarine", "fluorescent"} and texture in {"terracotta", "concrete", "concrete_powder", "wool"}):
				path = f"{folder}/{color}_{texture}.png"
				colorImage(baseTextures[texture], colors[color]).save(path)

genFolders()
genTextures()