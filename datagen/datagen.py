import json
import os

modid = "dyenamics"
paths = {"recipes": f"resources/data/{modid}/recipes",
	"block_loot": f"resources/data/{modid}/loot_tables/blocks",
	"sheep_loot": f"resources/data/{modid}/loot_tables/entities/sheep",
	"blockstates": f"resources/assets/{modid}/blockstates",
	"block_models": f"resources/assets/{modid}/models/block",
	"item_models": f"resources/assets/{modid}/models/item",
	"item_tags": f"resources/data/minecraft/tags/items",
	"block_tags": f"resources/data/minecraft/tags/blocks",
	"lang": f"resources/assets/{modid}/lang"
}

colors = ["peach", "aquamarine", "fluorescent", "mint", "maroon", "bubblegum", "lavender", "persimmon", "cherenkov"]
blocks = ["terracotta", "glazed_terracotta", "concrete", "concrete_powder", "wool", "carpet", "stained_glass", "stained_glass_pane"]
taggedBlocks = ["wool", "carpet"]
items = ["dye"]

#File Layout
def genFolders():
	for path in paths:
		os.makedirs(paths[path], exist_ok=True)

#en_us Lang
def genLang():
	lang = {}
	for color in colors:
		for item in items:
			name = color + "_" + item
			lang[f"item.{modid}.{name}"] = " ".join([word.capitalize() for word in name.split("_")])
		for block in blocks:
			name = color + "_" + block
			lang[f"block.{modid}.{name}"] = " ".join([word.capitalize() for word in name.split("_")])
	lang[f"entity.{modid}.sheep"] = "Sheep"
	with open(f"{paths['lang']}/en_us.json", "x") as file:
		file.write(json.dumps(lang, indent = 4))

#Recipes
def genRecipes():
	templates = {"wool": json.load(open("templates/wool_recipe.json")),
		"carpet": json.load(open("templates/carpet_recipe.json")),
		"carpetWhite": json.load(open("templates/carpet_from_white_recipe.json")),
		"powder": json.load(open("templates/powder_recipe.json")),
		"terracotta": json.load(open("templates/terracotta_recipe.json")),
		"glazed": json.load(open("templates/glazed_recipe.json")),
		"glass": json.load(open("templates/stained_recipe.json")),
		"pane": json.load(open("templates/stained_pane_recipe.json")),
		"paneGlass": json.load(open("templates/stained_pane_from_glass_pane_recipe.json"))
	}
	
	for color in colors:
		dye = f"{modid}:{color}_dye"
		for block in blocks:
			name = color + "_" + block
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
			elif block == "stained_glass":
				templates["glass"]["key"]["X"]["item"] = dye
				templates["glass"]["result"]["item"] = f"{modid}:{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["glass"], indent = 4))
			elif block == "stained_glass_pane":
				templates["pane"]["key"]["#"]["item"] = f"{modid}:{color}_stained_glass"
				templates["pane"]["result"]["item"] = f"{modid}:{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["pane"], indent = 4))
				templates["paneGlass"]["key"]["$"]["item"] = dye
				templates["paneGlass"]["result"]["item"] = f"{modid}:{name}"
				with open(f"{paths['recipes']}/{name}_from_glass_pane.json", "x") as file:
					file.write(json.dumps(templates["paneGlass"], indent = 4))

#Block Loot Tables
def genBlockLootTables():
	templates = {
		"block": json.load(open("templates/block_loot.json")),
		"glass": json.load(open("templates/glass_loot.json"))
	}
	for color in colors:
		for block in blocks:
			name = color + "_" + block
			if block.startswith("stained_glass"):
				templates["glass"]["pools"][0]["entries"][0]["name"] = f"{modid}:{name}"
				with open(f"{paths['block_loot']}/{name}.json", "x") as file:
					file.write(json.dumps(templates["glass"], indent = 4))
			else:
				templates["block"]["pools"][0]["entries"][0]["name"] = f"{modid}:{name}"
				with open(f"{paths['block_loot']}/{name}.json", "x") as file:
					file.write(json.dumps(templates["block"], indent = 4))

#Sheep Loot Tables
def genSheepLootTables():
	template = json.load(open("templates/sheep_loot.json"))
	for color in colors:
		template["pools"][0]["entries"][0]["name"] = f"{modid}:{color}_wool"
		with open(f"{paths['sheep_loot']}/{color}.json", "x") as file:
			file.write(json.dumps(template, indent = 4))

#Blockstates
def genBlockstates():
	templates = {
		"cube": json.load(open("templates/cube_blockstate.json")),
		"glazed": json.load(open("templates/glazed_blockstate.json")),
		"pane": json.load(open("templates/stained_pane_blockstate.json"))
	}
	
	for color in colors:
		for block in blocks:
			name = color + "_" + block
			path = f"{paths['blockstates']}/{name}.json"
			if block == "glazed_terracotta":
				for variant in templates["glazed"]["variants"]:
					templates["glazed"]["variants"][variant]["model"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["glazed"], indent = 4))
			elif block == "stained_glass_pane":
				for part in templates["pane"]["multipart"]:
					model = f"{modid}:block/{name}_"
					if "when" in part:
						direction = list(part["when"])[0]
						if part["when"][direction] == "false":
							model += "noside"
							if direction == "south" or direction == "east":
								model += "_alt"
						else:
							model += "side"
							if direction == "south" or direction == "west":
								model += "_alt"
					else:
						model += "post"
					part["apply"]["model"] = model
				with open(path, "x") as file:
					file.write(json.dumps(templates["pane"], indent = 4))
			else:
				templates["cube"]["variants"][""]["model"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["cube"], indent = 4))

#Block Models
def genBlockModels():
	templates = {
		"cube": json.load(open("templates/cube_model.json")),
		"carpet": json.load(open("templates/carpet_model.json")),
		"glazed": json.load(open("templates/glazed_model.json")),
		"pane": {
			"noside": json.load(open("templates/stained_pane_model1.json")),
			"noside_alt": json.load(open("templates/stained_pane_model2.json")),
			"post": json.load(open("templates/stained_pane_model3.json")),
			"side": json.load(open("templates/stained_pane_model4.json")),
			"side_alt": json.load(open("templates/stained_pane_model5.json")),
		}
	}
	
	for color in colors:
		for block in blocks:
			name = color + "_" + block
			path = f"{paths['block_models']}/{name}.json"
			if block == "glazed_terracotta":
				templates["glazed"]["textures"]["pattern"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["glazed"], indent = 4))
			elif block == "carpet":
				templates["carpet"]["textures"]["wool"] = f"{modid}:block/{color}_wool"
				with open(path, "x") as file:
					file.write(json.dumps(templates["carpet"], indent = 4))
			elif block == "stained_glass_pane":
				pane = f"{modid}:block/{color}_stained_glass"
				edge = f"{modid}:block/{color}_stained_glass_pane_top"
				for template in templates["pane"]:
					input = templates["pane"][template]["textures"]
					input["pane"] = pane
					if "edge" in input:
						input["edge"] = edge
					with open(f"{paths['block_models']}/{name}_{template}.json", "x") as file:
						file.write(json.dumps(templates["pane"][template], indent = 4))
			else:
				templates["cube"]["textures"]["all"] = f"{modid}:block/{name}"
				with open(path, "x") as file:
					file.write(json.dumps(templates["cube"], indent = 4))

#Item Models
def genItemModels():
	blockTemplate = json.load(open("templates/blockitem_model.json"))
	itemTemplate = json.load(open("templates/item_model.json"))
	for color in colors:
		for block in blocks:
			name = color + "_" + block
			if block == "stained_glass_pane":
				itemTemplate["textures"]["layer0"] = f"{modid}:block/{name}"
				with open(f"{paths['item_models']}/{name}.json", "x") as file:
					file.write(json.dumps(itemTemplate, indent = 4))
			else:
				blockTemplate["parent"] = f"{modid}:block/{name}"
				with open(f"{paths['item_models']}/{name}.json", "x") as file:
					file.write(json.dumps(blockTemplate, indent = 4))
		for item in items:
			name = color + "_" + item
			itemTemplate["textures"]["layer0"] = f"{modid}:item/{name}"
			with open(f"{paths['item_models']}/{name}.json", "x") as file:
				file.write(json.dumps(itemTemplate, indent = 4))

def genTags():
	template = json.load(open("templates/tag.json"))
	for block in taggedBlocks:
		ids = []
		for color in colors:
			ids.append(f"{modid}:{color}_{block}")
		template["values"] = ids
		with open(f"{paths['block_tags']}/{block}.json", "x") as file:
			file.write(json.dumps(template, indent = 4))
		with open(f"{paths['item_tags']}/{block}.json", "x") as file:
			file.write(json.dumps(template, indent = 4))
	

genFolders()
genLang()
genRecipes()
genBlockLootTables()
genSheepLootTables()
genBlockstates()
genBlockModels()
genItemModels()
genTags()