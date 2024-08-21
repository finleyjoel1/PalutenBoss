package com.finley.palutenboss.manager;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builder.FileBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MessageManager {
    //String = UUID , String = Language
    public final HashMap<String, String> playersLanguages;
    private final Map<String, Map<String, String>> languageMessages = new HashMap<>();
    private final List<String> validLanguages = Arrays.asList("de", "en", "ru", "es", "lt", "zh", "ja", "tr", "pl");
    private final FileBuilder messages = PalutenBoss.getInstance().getLoader().getMessageBuilder();
    private final FileBuilder languageFile;

    public MessageManager(FileBuilder languageFile) {
        this.languageFile = languageFile;

        languageFile.setPathIfEmpty("players", new HashMap<>());

        this.playersLanguages = new HashMap<>();

        Object rawData = languageFile.get("players");
        if (rawData instanceof MemorySection section) {
            for (String key : section.getKeys(false)) {
                this.getPlayersLanguages().put(key, section.getString(key));
            }
        }

        loadMessages();
    }

    private static void setPath(FileBuilder messages, String path, String value) {
        messages.setPathIfEmpty(path, value);
    }

    public void saveLanguagePerPlayer(Player player, String language) {
        this.getPlayersLanguages().put(player.getUniqueId().toString(), language);
        getLanguageFile().setPath("players", getPlayersLanguages());
    }

    public String getLanguageFromPlayer(Player player) {
        if (!this.getPlayersLanguages().containsKey(player.getUniqueId().toString())) {
            this.getPlayersLanguages().put(player.getUniqueId().toString(), PalutenBoss.getInstance().getLoader().getDefaultLanguage());
            saveLanguagePerPlayer(player, PalutenBoss.getInstance().getLoader().getDefaultLanguage());
            if (PalutenBoss.getInstance().isDebugMode()) {
                Bukkit.getLogger().warning("language from player not found (default is now english)");
            }
        }
        return this.getPlayersLanguages().get(player.getUniqueId().toString());
    }

    public void loadMessages() {
        for (String language : getValidLanguages()) {
            Map<String, String> messagesMap = new HashMap<>();
            for (String key : Arrays.asList("spawnSuccess", "noPermission", "noPlayer", "alert", "notFound",
                    "alreadyLanguage", "changeLanguage", "chooseLanguage", "bossSpawned", "reloadSuccess",
                    "cleanSuccess", "teamSuccess", "argumentError", "effectSuccess", "healthSuccess",
                    "motdSuccess", "showHealthSuccess", "changedSuccess", "gui.teleportLore")) {
                messagesMap.put(key, getMessages().getString(language + "." + key));
            }
            getLanguageMessages().put(language, messagesMap);
        }
    }

    public void sendMessageToPlayer(Player player, String messageKey) {
        sendMessage(player, ChatColor.translateAlternateColorCodes('&', getMessage(messageKey, getLanguageFromPlayer(player))));
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getPrefix() + message));
    }

    public void sendTitleToPlayer(Player player, String messageKey) {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', getMessage(messageKey, getLanguageFromPlayer(player))), "");
    }

    public void sendActionBar(Player player, String messageKey) {
        player.sendActionBar(Component.text(ChatColor.translateAlternateColorCodes('&', getMessage(messageKey, getLanguageFromPlayer(player)))));
    }

    public void sendNoPermissionMessage(Player player) {
        sendMessageToPlayer(player, "noPermission");
    }

    public void sendNoPlayerMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getPrefix() + getMessage("noPlayer", PalutenBoss.getInstance().getLoader().getDefaultLanguage())));
    }

    public String getMessage(String messageKey, String language) {
        Map<String, String> messages = getLanguageMessages().get(language);

        if (messages != null) {
            return messages.getOrDefault(messageKey, "§7This message not found.");
        } else {
            PalutenBoss.getInstance().getLoader().setDefaultLanguage("en");
            return "Invalid language";
        }
    }

    public boolean isValidLanguage(String language) {
        return getValidLanguages().contains(language);
    }

    public void saveMessages() {

        FileBuilder messages = getMessages();

        for (String language : getValidLanguages()) {
            String languagePrefix = language + ".";

            switch (language) {
                case "de":
                    setPath(messages, languagePrefix + "spawnSuccess", "&7Du hast &aerfolgreich &7den " + PalutenBoss.getInstance().getBossName() + " &7gespawnt.");
                    setPath(messages, languagePrefix + "noPermission", "&cDazu hast du keine Rechte!");
                    setPath(messages, languagePrefix + "noPlayer", "&cDu musst ein Spieler sein!");
                    setPath(messages, languagePrefix + "alert", "&c&lACHTUNG. &7Ein wildes &ePaluten ist erschienen.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7Du hast diese Sprache bereits ausgewählt.");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7Wähle deine Sprache.");
                    setPath(messages, languagePrefix + "notFound", "&7Dieses Argument konnte nicht gefunden werden.");
                    setPath(messages, languagePrefix + "changeLanguage", "&7Du hast &aerfolgreich &7die Sprache gesetzt.");
                    setPath(messages, languagePrefix + "bossSpawned", "&7Der " + PalutenBoss.getInstance().getBossName() + " &7wurde gespawnt.");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7Du hast &aerfolgreich &7die Config neugeladen.");
                    setPath(messages, languagePrefix + "cleanSuccess", "&7Du hast &aerfolgreich &7die &6Bosse &cgelöscht.");
                    setPath(messages, languagePrefix + "teamSuccess", "&7Du hast &aerfolgreich &7die &bTeam &cFarbe &7geändert.");
                    setPath(messages, languagePrefix + "argumentError", "&cDu hast ein Argument vergessen...");
                    setPath(messages, languagePrefix + "effectSuccess", "&7Du hast &aerfolgreich &7den &cEffekt &7geändert.");
                    setPath(messages, languagePrefix + "healthSuccess", "&7Du hast &aerfolgreich &7die &cLebensanzahl &7geändert.");
                    setPath(messages, languagePrefix + "motdSuccess", "&7Du hast &aerfolgreich &7die &bMOTD &7gesetzt.");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7Du hast &aerfolgreich &7die &bAnzeige &7gesetzt.");
                    setPath(messages, languagePrefix + "changedSuccess", "&7Du hast &aerfolgreich &7Änderungen vorgenommen.");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7Teleportiere dich zu &f%world%");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&lLeben");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&lEffekt");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lErschaffe &6&lPaluten&e&lBoss");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&lSprache");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&lConfig &b&lneuladen");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lTeamfarbe");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lLeere &6&lPaluten&e&lBosse");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&lZurück");
                    break;
                case "en":
                    //ENGLISH
                    setPath(messages, languagePrefix + "spawnSuccess", "&7You have successfully spawned the " + PalutenBoss.getInstance().getBossName() + ".");
                    setPath(messages, languagePrefix + "noPermission", "&cYou don't have permission to do this!");
                    setPath(messages, languagePrefix + "noPlayer", "&cYou have to be a player.");
                    setPath(messages, languagePrefix + "alert", "&c&lWARNING. &7A wild &ePaluten has appeared.");
                    setPath(messages, languagePrefix + "changeLanguage", "&7You have successfully set the language.");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7Choose your language.");
                    setPath(messages, languagePrefix + "notFound", "&7This argument could not be found.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7You have already selected this language.");
                    setPath(messages, languagePrefix + "bossSpawned", "&7The " + PalutenBoss.getInstance().getBossName() + " &7has been spawned.");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7You have successfully reloaded the config.");
                    setPath(messages, languagePrefix + "cleanSuccess", "&7You have successfully cleared the bosses.");
                    setPath(messages, languagePrefix + "teamSuccess", "&7You have successfully changed the team &bcolor&7.");
                    setPath(messages, languagePrefix + "argumentError", "&cYou forgot a argument...");
                    setPath(messages, languagePrefix + "effectSuccess", "&aYou have successfully changed the effect.");
                    setPath(messages, languagePrefix + "healthSuccess", "&7You have &asuccessfully &cchanged &7the number of lives.");
                    setPath(messages, languagePrefix + "motdSuccess", "&7You have &asuccessfully &7set &bMOTD.");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7You have &asuccessfully &7set the &bdisplay&7.");
                    setPath(messages, languagePrefix + "changedSuccess", "&7You have successfully &7made changes.");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7Teleport to &f%world%");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&lHealth");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&lEffect");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lSpawn &6&lPaluten&e&lBoss");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&lLanguage");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&lReload &b&lConfig");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lTeam Color");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lClear &6&lPaluten&e&lBosses");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&lBack");
                    break;
                case "ru":
                    //RUSSIA
                    setPath(messages, languagePrefix + "spawnSuccess", "&7Вы успешно вызвали &6&lПалутен&e&lБосс.");
                    setPath(messages, languagePrefix + "noPermission", "&cУ вас нет разрешения на это!");
                    setPath(messages, languagePrefix + "noPlayer", "&cВы должны быть игроком.");
                    setPath(messages, languagePrefix + "alert", "&c&lВНИМАНИЕ. &7Появился дикий Палутен.");
                    setPath(messages, languagePrefix + "changeLanguage", "&7Вы успешно установили язык.");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7Выберите свой язык.");
                    setPath(messages, languagePrefix + "notFound", "&7Этот аргумент не может быть найден.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7Вы уже выбрали этот язык.");
                    setPath(messages, languagePrefix + "bossSpawned", "&7" + PalutenBoss.getInstance().getBossName() + " &7был призван.");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7Вы успешно перезагрузили конфигурацию.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Вы успешно зачистили боссов.");
                    setPath(messages, languagePrefix + "teamSuccess", "&7Вы успешно изменили &bцвет&7.");
                    setPath(messages, languagePrefix + "argumentError", "&cВы забыли аргумент...");
                    setPath(messages, languagePrefix + "effectSuccess", "&aВы успешно изменили эффект.");
                    setPath(messages, languagePrefix + "healthSuccess", "&7Вы &aуспешно &cизменили &7количество жизней.");
                    setPath(messages, languagePrefix + "motdSuccess", "&7Вы &aуспешно &7установили &bMOTD.");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7Вы &aуспешно &7установили &bотображение&7.");
                    setPath(messages, languagePrefix + "changedSuccess", "&7Вы успешно &7внесли изменения.");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7Телепортироваться в &f%world%");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&lЗдоровье");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&lЭффект");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lПризвать &6&lПалутен&e&lБосса");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&lЯзык");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&lПерезагрузить &b&lКонфигурацию");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lЦвет команды");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lОчистить &6&lПалутен&e&lБоссов");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&lНазад");
                    break;
                case "es":
                    //Spanish
                    setPath(messages, languagePrefix + "spawnSuccess", "&7Has invocado exitosamente a " + PalutenBoss.getInstance().getBossName() + ".");
                    setPath(messages, languagePrefix + "noPermission", "&cNo tienes permiso para hacer esto.");
                    setPath(messages, languagePrefix + "noPlayer", "&cDebes ser un jugador.");
                    setPath(messages, languagePrefix + "alert", "&c&lADVERTENCIA. &7Un salvaje &ePaluten ha aparecido.");
                    setPath(messages, languagePrefix + "changeLanguage", "&7Has establecido el idioma con éxito.");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7Elige tu idioma.");
                    setPath(messages, languagePrefix + "notFound", "&7No se pudo encontrar este argumento.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7Ya has seleccionado este idioma.");
                    setPath(messages, languagePrefix + "bossSpawned", "&7El " + PalutenBoss.getInstance().getBossName() + " &7ha sido invocado.");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7Has recargado la configuración &aexitosamente&7.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Has eliminado los bosses.");
                    setPath(messages, languagePrefix + "teamSuccess", "&7Has cambiado la &bcolor&7.");
                    setPath(messages, languagePrefix + "argumentError", "&cOlvidaste un argumento...");
                    setPath(messages, languagePrefix + "effectSuccess", "&aHas cambiado el efecto exitosamente.");
                    setPath(messages, languagePrefix + "healthSuccess", "&7Has &acambiado con éxito &cel número de vidas.");
                    setPath(messages, languagePrefix + "motdSuccess", "&7Has &acambiado &7exitosamente el &bMOTD.");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7Has &acon éxito &7configurado la &bpantalla&7.");
                    setPath(messages, languagePrefix + "changedSuccess", "&7Has realizado cambios &7con éxito.");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7Teletransportarse a &f%world%");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&lSalud");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&lEfecto");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lInvocar &6&lPaluten&e&lJefe");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&lIdioma");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&lRecargar &b&lConfiguración");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lColor del equipo");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lLimpiar &6&lPaluten&e&lJefes");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&lAtrás");
                    break;
                case "lt":
                    //Lithuanian
                    setPath(messages, languagePrefix + "spawnSuccess", "&7Sėkmingai atvykote " + PalutenBoss.getInstance().getBossName() + ".");
                    setPath(messages, languagePrefix + "noPermission", "&cNeturite teisės tai daryti!");
                    setPath(messages, languagePrefix + "noPlayer", "&cJūs turite būti žaidėjas.");
                    setPath(messages, languagePrefix + "alert", "&c&lĮSPĖJIMAS. &7Atsirado laukinė &ePaluten.");
                    setPath(messages, languagePrefix + "changeLanguage", "&7Jūs sėkmingai nustatėte kalbą.");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7Pasirinkite savo kalbą.");
                    setPath(messages, languagePrefix + "notFound", "&7Šis argumentas nerastas.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7Jūs jau pasirinkote šią kalbą.");
                    setPath(messages, languagePrefix + "bossSpawned", "&7" + PalutenBoss.getInstance().getBossName() + " &7buvo atsivertęs.");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7Jūs sėkmingai įkėlėte konfigūraciją.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Sėkmingai išvalėte viršininkus.");
                    setPath(messages, languagePrefix + "teamSuccess", "&7Jūs sėkmingai nustatėte kalbą.");
                    setPath(messages, languagePrefix + "argumentError", "&cPamiršote argumentą...");
                    setPath(messages, languagePrefix + "effectSuccess", "&aJūs sėkmingai pakeitėte efektą.");
                    setPath(messages, languagePrefix + "healthSuccess", "&7Sėkmingai &apakeitėte &cgyvybių skaičių.");
                    setPath(messages, languagePrefix + "motdSuccess", "&7Sėkmingai &anustatėte &bMOTD.");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7Jūs &asėkmingai &7nustatėte &brodymą&7.");
                    setPath(messages, languagePrefix + "changedSuccess", "&7Jūs sėkmingai &7pakeitėte.");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7Teleportuotis į &f%world%");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&lSveikata");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&lPoveikis");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lIššaukti &6&lPaluten&e&lBosą");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&lKalba");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&lPaleisti iš naujo &b&lKonfigūraciją");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lKomandos spalva");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lIšvalyti &6&lPaluten&e&lBosus");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&lAtgal");
                    break;
                case "zh":
                    //Chinese
                    setPath(messages, languagePrefix + "spawnSuccess", "&7您已成功召唤了 " + PalutenBoss.getInstance().getBossName() + "。");
                    setPath(messages, languagePrefix + "noPermission", "&c您没有权限执行此操作！");
                    setPath(messages, languagePrefix + "noPlayer", "&c您必须是玩家。");
                    setPath(messages, languagePrefix + "alert", "&c&l警告：&7一只野生的&ePaluten 出现了。");
                    setPath(messages, languagePrefix + "changeLanguage", "&7您已成功设置语言。");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7选择您的语言。");
                    setPath(messages, languagePrefix + "notFound", "&7找不到此参数。");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7您已经选择了此语言。");
                    setPath(messages, languagePrefix + "bossSpawned", "&7" + PalutenBoss.getInstance().getBossName() + " &7已经被召唤。");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7您已成功重新加载配置。");
                    setPath(messages, languagePrefix + "cleanSuccess", "您已成功清除boss。");
                    setPath(messages, languagePrefix + "argumentError", "&c您忘记了一个参数...");
                    setPath(messages, languagePrefix + "effectSuccess", "&a您已成功更改效果。");
                    setPath(messages, languagePrefix + "teamSuccess", "&7您已成功更改颜色。");
                    setPath(messages, languagePrefix + "healthSuccess", "&7你已成功&a更改了&c生命数量。");
                    setPath(messages, languagePrefix + "motdSuccess", "&7你已成功&a设置了&bMOTD。");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7你已&a成功&7设置了&b显示&7。");
                    setPath(messages, languagePrefix + "changedSuccess", "&7你已成功&7进行了更改。");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7传送到 &f%world%");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&l生命");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&l效果");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&l生成 &6&lPaluten&e&l首领");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&l语言");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&l重新加载 &b&l配置");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&l队伍颜色");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&l清除 &6&lPaluten&e&l首领");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&l返回");
                    break;
                case "ja":
                    //Japanish
                    setPath(messages, languagePrefix + "spawnSuccess", "&7" + PalutenBoss.getInstance().getBossName() + "を召喚に成功しました。");
                    setPath(messages, languagePrefix + "noPermission", "&cこの操作を行う権限がありません！");
                    setPath(messages, languagePrefix + "noPlayer", "&cプレイヤーである必要があります。");
                    setPath(messages, languagePrefix + "alert", "&c&l警告：&7野生の&ePalutenが現れました。");
                    setPath(messages, languagePrefix + "changeLanguage", "&7言語を正常に設定しました。");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7言語を選択してください。");
                    setPath(messages, languagePrefix + "notFound", "&7この引数が見つかりませんでした。");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7あなたはすでにこの言語を選択しています。");
                    setPath(messages, languagePrefix + "bossSpawned", "&7" + PalutenBoss.getInstance().getBossName() + " &7が召喚されました。");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7設定を&a正常に&7リロードしました。");
                    setPath(messages, languagePrefix + "cleanSuccess", "&7bossをクリーンしました。");
                    setPath(messages, languagePrefix + "argumentError", "&c引数を忘れています...");
                    setPath(messages, languagePrefix + "effectSuccess", "&a効果を変更しました。成功しました。");
                    setPath(messages, languagePrefix + "teamSuccess", "&7色を正常に設定しました。");
                    setPath(messages, languagePrefix + "healthSuccess", "&7あなたは&a正常に&cライフ数を変更しました。");
                    setPath(messages, languagePrefix + "motdSuccess", "&7あなたは&a正常に&bMOTDを設定しました。");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7あなたは&a成功&7して&b表示&7を設定しました。");
                    setPath(messages, languagePrefix + "changedSuccess", "&7変更が&7正常に完了しました。");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7&f%world% &7にテレポート");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&l健康");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&l効果");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lスポーン &6&lパルテン&e&lボス");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&l言語");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&l再読み込み &b&l設定");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lチームカラー");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lクリア &6&lパルテン&e&lボス");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&l戻る");
                    break;
                case "tr":
                    //Turkey
                    setPath(messages, languagePrefix + "spawnSuccess", "&7" + PalutenBoss.getInstance().getBossName() + " başarıyla oluşturuldu.");
                    setPath(messages, languagePrefix + "noPermission", "&cBunu yapmak için izniniz yok!");
                    setPath(messages, languagePrefix + "noPlayer", "&cBir oyuncu olmalısınız.");
                    setPath(messages, languagePrefix + "alert", "&c&lUYARI. &7Vahşi &ePaluten belirdi.");
                    setPath(messages, languagePrefix + "changeLanguage", "&7Diliniz başarıyla değiştirildi.");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7Dilinizi seçin.");
                    setPath(messages, languagePrefix + "notFound", "&7Bu argüman bulunamadı.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7Zaten bu dili seçtiniz.");
                    setPath(messages, languagePrefix + "bossSpawned", "&7" + PalutenBoss.getInstance().getBossName() + " başarıyla oluşturuldu.");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7Yapılandırma başarıyla yeniden yüklendi.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Boss'lar başarıyla temizlendi.");
                    setPath(messages, languagePrefix + "teamSuccess", "&7Takım &brenk&7 başarıyla değiştirildi.");
                    setPath(messages, languagePrefix + "argumentError", "&cBir argüman unuttunuz...");
                    setPath(messages, languagePrefix + "effectSuccess", "&aEfekt başarıyla değiştirildi.");
                    setPath(messages, languagePrefix + "healthSuccess", "&7Başarıyla &acanlı sayısını &cdeğiştirdiniz.");
                    setPath(messages, languagePrefix + "motdSuccess", "&7Başarıyla &bMOTD &7ayarladınız.");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7Başarıyla &a&7&bgöstergeyi &7ayarladınız.");
                    setPath(messages, languagePrefix + "changedSuccess", "&7Başarıyla &7değişiklik yaptınız.");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7&f%world% &7'ye ışınlan");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&lSağlık");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&lEtki");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lSpawn &6&lPaluten&e&lBoss");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&lDil");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&lYeniden Yükle &b&lYapılandırma");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lTakım Rengi");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lTemizle &6&lPaluten&e&lBosslar");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&lGeri");
                    break;
                case "pl":
                    setPath(messages, languagePrefix + "spawnSuccess", "&7Pomyślnie &astworzono &7" + PalutenBoss.getInstance().getBossName() + " &7.");
                    setPath(messages, languagePrefix + "noPermission", "&cNie masz do tego uprawnień!");
                    setPath(messages, languagePrefix + "noPlayer", "&cMusisz być graczem!");
                    setPath(messages, languagePrefix + "alert", "&c&lUWAGA. &7Pojawił się dziki &ePaluten.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "&7Wybrałeś już ten język.");
                    setPath(messages, languagePrefix + "chooseLanguage", "&7Wybierz swój język.");
                    setPath(messages, languagePrefix + "notFound", "&7Nie znaleziono tego argumentu.");
                    setPath(messages, languagePrefix + "changeLanguage", "&7Pomyślnie &austawiono &7język.");
                    setPath(messages, languagePrefix + "bossSpawned", "&7" + PalutenBoss.getInstance().getBossName() + " &7został stworzony.");
                    setPath(messages, languagePrefix + "reloadSuccess", "&7Pomyślnie &aodświeżono &7konfigurację.");
                    setPath(messages, languagePrefix + "cleanSuccess", "&7Pomyślnie &ausunąłeś &7" + "&6Bossy &c.");
                    setPath(messages, languagePrefix + "teamSuccess", "&7Pomyślnie &austawiono &7" + "&bKolor drużyny &c.");
                    setPath(messages, languagePrefix + "argumentError", "&cZapomniałeś o argumencie...");
                    setPath(messages, languagePrefix + "effectSuccess", "&7Pomyślnie &austawiono &7" + "&cEfekt &7.");
                    setPath(messages, languagePrefix + "healthSuccess", "&7Pomyślnie &austawiono &7" + "&cLiczba żyć &7.");
                    setPath(messages, languagePrefix + "motdSuccess", "&7Pomyślnie &austawiono &7" + "&bMOTD &7.");
                    setPath(messages, languagePrefix + "showHealthSuccess", "&7Pomyślnie &austawiono &7" + "&bWyświetlanie &7.");
                    setPath(messages, languagePrefix + "changedSuccess", "&7Pomyślnie &7wprowadziłeś zmiany.");
                    setPath(messages, languagePrefix + "gui.teleportLore", "&7Teleportuj się do &f%world%");
                    setPath(messages, languagePrefix + "gui.item.healthItem", "&c&lŻycie");
                    setPath(messages, languagePrefix + "gui.item.effectItem", "&6&lEfekt");
                    setPath(messages, languagePrefix + "gui.item.spawnItem", "&a&lStwórz &6&lPaluten&e&lBoss");
                    setPath(messages, languagePrefix + "gui.item.languageItem", "&a&lJęzyk");
                    setPath(messages, languagePrefix + "gui.item.configItem", "&f&lKonfiguracja &b&lodśwież");
                    setPath(messages, languagePrefix + "gui.item.teamItem", "&d&lKolor drużyny");
                    setPath(messages, languagePrefix + "gui.item.cleanItem", "&c&lWyczyść &6&lPaluten&e&lBossy");
                    setPath(messages, languagePrefix + "gui.item.backItem", "&c&lWstecz");

                    //"ChatColor.translateAlternateColorCodes('&',PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem"));"
                    break;
            }

            PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
        }
    }

}