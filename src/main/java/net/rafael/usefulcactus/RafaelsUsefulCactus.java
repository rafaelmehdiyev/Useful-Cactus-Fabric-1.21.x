package net.rafael.usefulcactus;

import net.fabricmc.api.ModInitializer;

import net.rafael.usefulcactus.block.ModBlocks;
import net.rafael.usefulcactus.item.ModItemGroups;
import net.rafael.usefulcactus.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RafaelsUsefulCactus implements ModInitializer {
	public static final String MOD_ID = "rafaels-useful-cactus";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
	}
}