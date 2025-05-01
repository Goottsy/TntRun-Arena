package com.goottsy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.goottsy.PluginTemplate;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

@CommandAlias("main")
@CommandPermission("admin.perm")
public class MainCMD extends BaseCommand {
    private FileConfiguration config;
    private PluginTemplate instance;

    public MainCMD(PluginTemplate instance) {
        this.instance = instance;
    }

    @Subcommand("command")
    @CommandAlias("screen-command")
    @CommandCompletion("")
    public void command(CommandSender sender) {

    }
}
