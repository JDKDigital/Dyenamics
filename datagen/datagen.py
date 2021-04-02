import json
import os

modid = "thermal_dyenamics"
paths = {"recipes": f"resources/data/{modid}/recipes",
	"loot_tables": f"resources/data/{modid}/loot_tables/blocks",
	"blockstates": f"resources/assets/{modid}/blockstates",
	"block_models": f"resources/assets/{modid}/models/block",
	"item_models": f"resources/assets/{modid}/models/item",
	"lang": f"resources/assets/{modid}/lang"
}

dyes = ["peach", "aquamarine", "fluorescent"]
blocks = ["terracotta", "glazed_terracotta", "concrete", "concrete_powder", "wool", "carpet"]
items = ["dye"]

#File Layout
def genFolders():
	for path in paths:
		os.makedirs(paths[path], exist_ok=True)

#en_us Lang
def genLang():
	lang = {}
	for dye in dyes:
		for item in items:
			name = dye + "_" + item
			lang[f"item.{modid}.{name}"] = " ".join([word.capitalize() for word in name.split("_")])
		for block in blocks:
			name = dye + "_" + block
			lang[f"block.{modid}.{name}"] = " ".join([word.capitalize() for word in name.split("_")])
	with open(f"{paths['lang']}/en_us.json", "x") as file:
		file.write(json.dumps(lang, indent = 4))

#Recipes
def genRecipes():
	templates = {"wool": json.load(open("templates/wool_recipe.json")),
		"carpet": json.load(open("templates/carpet_recipe.json")),
		"carpetWhite": json.load(open("templates/carpet_from_white_recipe.json")),
		"powder": json.load(open("templates/powder_recipe.json")),
		"terracotta": json.load(open("templates/terracotta_recipe.json")),
		"glazed": json.load(open("templates/glazed_recipe.json"))
	}
	
	for color in dyes:
		for block in blocks:
			name = color + "_" + block
			dye = color + "_dye"
			path = f"{paths['recipes']}/{name}.json"
			if block == "terracotta":
				templates["terracotta"]["key"]["X"]["item"] = dye
				templates["terracotta"]["result"]["item"] = f"{modid}:{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["terracotta"], indent = 4))
			elif block == "glazed_terracotta":
				templates["glazed"]["ingredient"]["item"] = f"{modid}:{color}_terracotta"
				templates["glazed"]["result"] = f"{modid}:{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["glazed"], indent = 4))
			elif block == "concrete_powder":
				templates["powder"]["ingredients"][0]["item"] = dye
				templates["powder"]["result"]["item"] = f"{modid}:{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["powder"], indent = 4))
			elif block == "wool":
				templates["wool"]["ingredients"][0]["item"] = dye
				templates["wool"]["result"]["item"] = f"{modid}:{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["wool"], indent = 4))
			elif block == "carpet":
				templates["carpet"]["key"]["#"]["item"] = f"{modid}:{color}_wool"
				templates["carpet"]["result"]["item"] = f"{modid}:{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["carpet"], indent = 4))
				templates["carpetWhite"]["key"]["$"]["item"] = dye
				templates["carpetWhite"]["result"]["item"] = f"{modid}:{name}"
				with open(f"{paths['recipes']}/{name}_from_white_carpet.json", "x") as file:
					file.write(json.dumps(templates["carpetWhite"], indent = 4))

#Loot Tables
def genLootTables():
	lootTemplate = json.load(open("templates/block_loot.json"))
	for dye in dyes:
		for block in blocks:
			name = dye + "_" + block
			lootTemplate["pools"][0]["entries"][0]["name"] = f"{modid}:{name}"
			with open(f"{paths['loot_tables']}/{name}.json", "x") as file:
				file.write(json.dumps(lootTemplate, indent = 4))

#Blockstates
def genBlockstates():
	cubeTemplate = json.load(open("templates/cube_blockstate.json"))
	glazedTemplate = json.load(open("templates/glazed_blockstate.json"))
	for dye in dyes:
		for block in blocks:
			name = dye + "_" + block
			path = f"{paths['blockstates']}/{name}.json"
			if block == "glazed_terracotta":
				for variant in glazedTemplate["variants"]:
					glazedTemplate["variants"][variant]["model"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(glazedTemplate, indent = 4))
			else:
				cubeTemplate["variants"][""]["model"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(cubeTemplate, indent = 4))

#Block Models
def genBlockModels():
	cubeTemplate = json.load(open("templates/cube_model.json"))
	carpetTemplate = json.load(open("templates/carpet_model.json"))
	glazedTemplate = json.load(open("templates/glazed_model.json"))
	for dye in dyes:
		for block in blocks:
			name = dye + "_" + block
			path = f"{paths['block_models']}/{name}.json"
			if block == "glazed_terracotta":
				glazedTemplate["textures"]["pattern"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(glazedTemplate, indent = 4))
			elif block == "carpet":
				carpetTemplate["textures"]["wool"] = f"{modid}:block/{dye}_wool"
				with open(path, "x") as file:
					file.write(json.dumps(carpetTemplate, indent = 4))
			else:
				cubeTemplate["textures"]["all"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(cubeTemplate, indent = 4))

#Item Models
def genItemModels():
	blockTemplate = json.load(open("templates/blockitem_model.json"))
	itemTemplate = json.load(open("templates/item_model.json"))
	for dye in dyes:
		for block in blocks:
			name = dye + "_" + block
			blockTemplate["parent"] = f"{modid}:block/{name}"
			with open(f"{paths['item_models']}/{name}.json", "x") as file:
				file.write(json.dumps(blockTemplate, indent = 4))
		for item in items:
			name = dye + "_" + item
			itemTemplate["textures"]["layer0"] = f"{modid}:item/{name}"
			with open(f"{paths['item_models']}/{name}.json", "x") as file:
				file.write(json.dumps(itemTemplate, indent = 4))

genFolders()
genLang()
genRecipes()
genLootTables()
genBlockstates()
genBlockModels()
genItemModels()