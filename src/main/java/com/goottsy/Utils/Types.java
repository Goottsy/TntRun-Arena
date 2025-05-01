package com.goottsy.Utils;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import com.google.common.collect.ImmutableList;
import com.goottsy.PluginTemplate;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class Types {
    PluginTemplate instance;

    public Types(PluginTemplate instance){
        this.instance = instance;
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = PluginTemplate.commandManager.getCommandCompletions();
//        commandCompletions.registerAsyncCompletion("items", c -> {
//            Collection<String> items = Arrays.asList(Game.Items.values()).stream().map(e -> e.toString()).collect(Collectors.toList());
//            return items;
//        });


        commandCompletions.registerAsyncCompletion("materials", c -> {
            Collection<String> materials = Arrays.stream(Material.values())
                    .map(e -> e.toString().toLowerCase())  // Convertir a minúsculas
                    .collect(Collectors.toList());


            return materials;
        });


        commandCompletions.registerAsyncCompletion("bool", c -> {
            return ImmutableList.of("true", "false");
        });

    }




}
