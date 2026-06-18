package ui;

import items.HPPot;
import items.Item;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

final class ImageAssets {
    private static final String ATTACK_BUTTON = "attack-button-2.png";
    private static final String COMMON_HEALING_POTION = "common-healing-potion.png";
    private static final String UNCOMMON_HEALING_POTION = "uncommon-healing-potion.png";
    private static final String RARE_HEALING_POTION = "rare-healing-potion.png";
    private static final String EPIC_HEALING_POTION = "epic-healing-potion.png";
    private static final String LEGENDARY_HEALING_POTION = "legendary-healing-potion.png";
    private static final String GOLD_COIN = "gold-coin.png";
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();

    private ImageAssets() {
    }

    static void applyAttackButtonGraphic(Button button) {
        ImageView graphic = imageView(ATTACK_BUTTON, 56, 56);

        if (graphic == null) {
            return;
        }

        button.setText("");
        button.setGraphic(graphic);
        button.setMinSize(56, 56);
        button.setPrefSize(56, 56);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
    }

    static void setGoldAmount(Label label, int amount) {
        setGoldAmount(label, "", amount);
    }

    static void setGoldAmount(Label label, int amount, double iconSize) {
        setGoldAmount(label, "", amount, iconSize);
    }

    static void setGoldAmount(Label label, String prefix, int amount) {
        setGoldAmount(label, prefix, amount, 18);
    }

    static void setGoldAmount(Label label, String prefix, int amount, double iconSize) {
        label.setText(prefix + amount);
        label.setGraphic(imageView(GOLD_COIN, iconSize, iconSize));
        label.setGraphicTextGap(5);
    }

    static ImageView itemIcon(Item item, double size) {
        if (item instanceof HPPot) {
            return imageView(healingPotionIconName(item), size, size);
        }

        return null;
    }

    private static String healingPotionIconName(Item item) {
        return switch (item.getRarity()) {
            case 'U' -> UNCOMMON_HEALING_POTION;
            case 'R' -> RARE_HEALING_POTION;
            case 'E' -> EPIC_HEALING_POTION;
            case 'L' -> LEGENDARY_HEALING_POTION;
            default -> COMMON_HEALING_POTION;
        };
    }

    private static ImageView imageView(String filename, double fitWidth, double fitHeight) {
        Image image = load(filename);

        if (image == null) {
            return null;
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        return imageView;
    }

    private static Image load(String filename) {
        if (IMAGE_CACHE.containsKey(filename)) {
            return IMAGE_CACHE.get(filename);
        }

        Image image = loadImage(filename);
        IMAGE_CACHE.put(filename, image);
        return image;
    }

    private static Image loadImage(String filename) {
        URL resource = ImageAssets.class.getResource("/assets/" + filename);

        if (resource != null) {
            return new Image(resource.toExternalForm());
        }

        File assetFile = new File("assets", filename);
        if (assetFile.isFile()) {
            return new Image(assetFile.toURI().toString());
        }

        File misspelledAssetFile = new File("Assests", filename);
        if (misspelledAssetFile.isFile()) {
            return new Image(misspelledAssetFile.toURI().toString());
        }

        return null;
    }
}
