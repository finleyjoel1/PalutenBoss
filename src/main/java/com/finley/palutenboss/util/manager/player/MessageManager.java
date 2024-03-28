package com.finley.palutenboss.util.manager.player;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.Loader;
import com.finley.palutenboss.util.builders.FileBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManager {
    public final Map<String, Map<String, String>> languageMessages = new HashMap<>();
    private final List<String> validLanguages = Arrays.asList("de", "en", "ru", "es", "lt", "zh", "ja", "tr");
    private FileBuilder messages = PalutenBoss.getInstance().getLoader().getMessages();

    public MessageManager() {
        loadMessages();
    }

    private static void setPath(FileBuilder messages, String path, String value) {
        messages.setPathIfEmpty(path, value);
    }

    private void loadMessages() {
        for (String language : validLanguages) {
            Map<String, String> messagesMap = new HashMap<>();
            for (String key : Arrays.asList("spawnSuccess", "noPermission", "noPlayer", "alert", "notFound",
                    "alreadyLanguage", "changeLanguage", "chooseLanguage", "bossSpawned", "reloadSuccess",
                    "cleanSuccess", "teamSuccess", "argumentError", "effectSuccess")) {
                messagesMap.put(key, messages.getString(language + "." + key));
            }
            languageMessages.put(language, messagesMap);
        }
    }

    public void sendMessageToPlayer(Player player, String messageKey) {
        sendMessage(player, getMessage(messageKey, Loader.language));
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(PalutenBoss.getInstance().getPrefix() + message);
    }

    public void sendTitleToPlayer(Player player, String messageKey) {
        player.sendTitle(getMessage(messageKey, Loader.language), "");
    }

    public void sendNoPermissionMessage(Player player) {
        sendMessageToPlayer(player, "noPermission");
    }

    public void sendNoPlayerMessage() {
        Bukkit.getConsoleSender().sendMessage(PalutenBoss.getInstance().getPrefix() + getMessage("noPlayer", Loader.language));
    }

    private String getMessage(String messageKey, String language) {
        Map<String, String> messages = languageMessages.get(language);

        if (messages != null) {
            return messages.getOrDefault(messageKey, "This message not found.");
        } else {
            Loader.language = "en";
            return "Invalid language";
        }
    }

    public boolean isValidLanguage(String language) {
        return validLanguages.contains(language);
    }

    public FileBuilder getMessages() {
        return messages;
    }

    public void saveMessages() {
        FileBuilder messages = getMessages();

        for (String language : validLanguages) {
            String languagePrefix = language + ".";

            switch (language) {
                case "de":
                    setPath(messages, languagePrefix + "spawnSuccess", "§7Du hast §aerfolgreich §7den " + PalutenBoss.getInstance().getBossName() + " §7gespawnt.");
                    setPath(messages, languagePrefix + "noPermission", "§cDazu hast du keine Rechte!");
                    setPath(messages, languagePrefix + "noPlayer", "§cDu musst ein Spieler sein!");
                    setPath(messages, languagePrefix + "alert", "§c§lACHTUNG. §7Ein wildes §ePaluten ist erschienen.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7Du hast diese Sprache bereits ausgewählt.");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7Wähle deine Sprache.");
                    setPath(messages, languagePrefix + "notFound", "§7Dieses Argument konnte nicht gefunden werden.");
                    setPath(messages, languagePrefix + "changeLanguage", "§7Du hast §aerfolgreich §7die Sprache gesetzt.");
                    setPath(messages, languagePrefix + "bossSpawned", "§7Der " + PalutenBoss.getInstance().getBossName() + " §7wurde gespawnt.");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7Du hast §aerfolgreich §7die Config neugeladen.");
                    setPath(messages, languagePrefix + "cleanSuccess", "§7Du hast §aerfolgreich §7die §6Bosse §cgelöscht.");
                    setPath(messages, languagePrefix + "teamSuccess", "§7Du hast §aerfolgreich §7die §bTeam §cFarbe §7geändert.");
                    setPath(messages, languagePrefix + "argumentError", "§cDu hast ein Argument vergessen...");
                    setPath(messages, languagePrefix + "effectSuccess", "§7Du hast §aerfolgreich den §cEffekt §7geändert.");
                    break;
                case "en":
                    //ENGLISH
                    setPath(messages, languagePrefix + "spawnSuccess", "§7You have successfully spawned the " + PalutenBoss.getInstance().getBossName() + ".");
                    setPath(messages, languagePrefix + "noPermission", "§cYou don't have permission to do this!");
                    setPath(messages, languagePrefix + "noPlayer", "§cYou have to be a player.");
                    setPath(messages, languagePrefix + "alert", "§c§lWARNING. §7A wild §ePaluten has appeared.");
                    setPath(messages, languagePrefix + "changeLanguage", "§7You have successfully set the language.");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7Choose your language.");
                    setPath(messages, languagePrefix + "notFound", "§7This argument could not be found.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7You have already selected this language.");
                    setPath(messages, languagePrefix + "bossSpawned", "§7The " + PalutenBoss.getInstance().getBossName() + " §7has been spawned.");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7You have successfully reloaded the config.");
                    setPath(messages, languagePrefix + "cleanSuccess", "You have successfully cleared the bosses.");
                    setPath(messages, languagePrefix + "teamSuccess", "§7You have successfully changed the team §bcolor§7.");
                    setPath(messages, languagePrefix + "argumentError", "§cYou forgot a argument...");
                    setPath(messages, languagePrefix + "effectSuccess", "§aYou have successfully changed the effect.");
                    break;
                case "ru":
                    //RUSSIA
                    setPath(messages, languagePrefix + "spawnSuccess", "§7Вы успешно вызвали §6§lПалутен§e§lБосс.");
                    setPath(messages, languagePrefix + "noPermission", "§cУ вас нет разрешения на это!");
                    setPath(messages, languagePrefix + "noPlayer", "§cВы должны быть игроком.");
                    setPath(messages, languagePrefix + "alert", "§c§lВНИМАНИЕ. §7Появился дикий Палутен.");
                    setPath(messages, languagePrefix + "changeLanguage", "§7Вы успешно установили язык.");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7Выберите свой язык.");
                    setPath(messages, languagePrefix + "notFound", "§7Этот аргумент не может быть найден.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7Вы уже выбрали этот язык.");
                    setPath(messages, languagePrefix + "bossSpawned", "§7" + PalutenBoss.getInstance().getBossName() + " §7был призван.");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7Вы успешно перезагрузили конфигурацию.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Вы успешно зачистили боссов.");
                    setPath(messages, languagePrefix + "teamSuccess", "§7Вы успешно изменили §bцвет§7.");
                    setPath(messages, languagePrefix + "argumentError", "§cВы забыли аргумент...");
                    setPath(messages, languagePrefix + "effectSuccess", "§aВы успешно изменили эффект.");
                    break;
                case "es":
                    setPath(messages, languagePrefix + "spawnSuccess", "§7Has invocado exitosamente a " + PalutenBoss.getInstance().getBossName() + ".");
                    setPath(messages, languagePrefix + "noPermission", "§cNo tienes permiso para hacer esto.");
                    setPath(messages, languagePrefix + "noPlayer", "§cDebes ser un jugador.");
                    setPath(messages, languagePrefix + "alert", "§c§lADVERTENCIA. §7Un salvaje §ePaluten ha aparecido.");
                    setPath(messages, languagePrefix + "changeLanguage", "§7Has establecido el idioma con éxito.");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7Elige tu idioma.");
                    setPath(messages, languagePrefix + "notFound", "§7No se pudo encontrar este argumento.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7Ya has seleccionado este idioma.");
                    setPath(messages, languagePrefix + "bossSpawned", "§7El " + PalutenBoss.getInstance().getBossName() + " §7ha sido invocado.");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7Has recargado la configuración §aexitosamente§7.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Has eliminado los bosses.");
                    setPath(messages, languagePrefix + "teamSuccess", "§7Has cambiado la §bcolor§7.");
                    setPath(messages, languagePrefix + "argumentError", "§cOlvidaste un argumento...");
                    setPath(messages, languagePrefix + "effectSuccess", "§aHas cambiado el efecto exitosamente.");
                    break;
                case "lt":
                    //Lithuanian
                    setPath(messages, languagePrefix + "spawnSuccess", "§7Sėkmingai atvykote " + PalutenBoss.getInstance().getBossName() + ".");
                    setPath(messages, languagePrefix + "noPermission", "§cNeturite teisės tai daryti!");
                    setPath(messages, languagePrefix + "noPlayer", "§cJūs turite būti žaidėjas.");
                    setPath(messages, languagePrefix + "alert", "§c§lĮSPĖJIMAS. §7Atsirado laukinė §ePaluten.");
                    setPath(messages, languagePrefix + "changeLanguage", "§7Jūs sėkmingai nustatėte kalbą.");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7Pasirinkite savo kalbą.");
                    setPath(messages, languagePrefix + "notFound", "§7Šis argumentas nerastas.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7Jūs jau pasirinkote šią kalbą.");
                    setPath(messages, languagePrefix + "bossSpawned", "§7" + PalutenBoss.getInstance().getBossName() + " §7buvo atsivertęs.");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7Jūs sėkmingai įkėlėte konfigūraciją.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Sėkmingai išvalėte viršininkus.");
                    setPath(messages, languagePrefix + "teamSuccess", "§7Jūs sėkmingai nustatėte kalbą.");
                    setPath(messages, languagePrefix + "argumentError", "§cPamiršote argumentą...");
                    setPath(messages, languagePrefix + "effectSuccess", "§aJūs sėkmingai pakeitėte efektą.");
                    break;
                case "zh":
                    setPath(messages, languagePrefix + "spawnSuccess", "§7您已成功召唤了 " + PalutenBoss.getInstance().getBossName() + "。");
                    setPath(messages, languagePrefix + "noPermission", "§c您没有权限执行此操作！");
                    setPath(messages, languagePrefix + "noPlayer", "§c您必须是玩家。");
                    setPath(messages, languagePrefix + "alert", "§c§l警告：§7一只野生的§ePaluten 出现了。");
                    setPath(messages, languagePrefix + "changeLanguage", "§7您已成功设置语言。");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7选择您的语言。");
                    setPath(messages, languagePrefix + "notFound", "§7找不到此参数。");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7您已经选择了此语言。");
                    setPath(messages, languagePrefix + "bossSpawned", "§7" + PalutenBoss.getInstance().getBossName() + " §7已经被召唤。");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7您已成功重新加载配置。");
                    setPath(messages, languagePrefix + "cleanSuccess", "您已成功清除boss。");
                    setPath(messages, languagePrefix + "argumentError", "§c您忘记了一个参数...");
                    setPath(messages, languagePrefix + "effectSuccess", "§a您已成功更改效果。");
                    setPath(messages, languagePrefix + "teamSuccess", "§7您已成功更改颜色。");
                    break;
                case "ja":
                    setPath(messages, languagePrefix + "spawnSuccess", "§7" + PalutenBoss.getInstance().getBossName() + "を召喚に成功しました。");
                    setPath(messages, languagePrefix + "noPermission", "§cこの操作を行う権限がありません！");
                    setPath(messages, languagePrefix + "noPlayer", "§cプレイヤーである必要があります。");
                    setPath(messages, languagePrefix + "alert", "§c§l警告：§7野生の§ePalutenが現れました。");
                    setPath(messages, languagePrefix + "changeLanguage", "§7言語を正常に設定しました。");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7言語を選択してください。");
                    setPath(messages, languagePrefix + "notFound", "§7この引数が見つかりませんでした。");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7あなたはすでにこの言語を選択しています。");
                    setPath(messages, languagePrefix + "bossSpawned", "§7" + PalutenBoss.getInstance().getBossName() + " §7が召喚されました。");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7設定を§a正常に§7リロードしました。");
                    setPath(messages, languagePrefix + "cleanSuccess", "§7bossをクリーンしました。");
                    setPath(messages, languagePrefix + "teamSuccess", "§7色を正常に設定しました。");
                    setPath(messages, languagePrefix + "argumentError", "§c引数を忘れています...");
                    setPath(messages, languagePrefix + "effectSuccess", "§a効果を変更しました。成功しました。");
                    break;
                case "tr":
                    setPath(messages, languagePrefix + "spawnSuccess", "§7" + PalutenBoss.getInstance().getBossName() + " başarıyla oluşturuldu.");
                    setPath(messages, languagePrefix + "noPermission", "§cBunu yapmak için izniniz yok!");
                    setPath(messages, languagePrefix + "noPlayer", "§cBir oyuncu olmalısınız.");
                    setPath(messages, languagePrefix + "alert", "§c§lUYARI. §7Vahşi §ePaluten belirdi.");
                    setPath(messages, languagePrefix + "changeLanguage", "§7Diliniz başarıyla değiştirildi.");
                    setPath(messages, languagePrefix + "chooseLanguage", "§7Dilinizi seçin.");
                    setPath(messages, languagePrefix + "notFound", "§7Bu argüman bulunamadı.");
                    setPath(messages, languagePrefix + "alreadyLanguage", "§7Zaten bu dili seçtiniz.");
                    setPath(messages, languagePrefix + "bossSpawned", "§7" + PalutenBoss.getInstance().getBossName() + " başarıyla oluşturuldu.");
                    setPath(messages, languagePrefix + "reloadSuccess", "§7Yapılandırma başarıyla yeniden yüklendi.");
                    setPath(messages, languagePrefix + "cleanSuccess", "Boss'lar başarıyla temizlendi.");
                    setPath(messages, languagePrefix + "teamSuccess", "§7Takım §brenk§7 başarıyla değiştirildi.");
                    setPath(messages, languagePrefix + "argumentError", "§cBir argüman unuttunuz...");
                    setPath(messages, languagePrefix + "effectSuccess", "§aEfekt başarıyla değiştirildi.");
                    break;
            }

            PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
        }
    }

}
