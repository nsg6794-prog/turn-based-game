package ui;

import game.Player;
import items.HPPot;
import items.Item;
import items.Weapon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

public class InventoryUI extends BorderPane {
    private final Player player;
    private final Runnable returnToPrevious;
    private final Label healthLabel = new Label();
    private final Label messageLabel = new Label();
    private final VBox itemList = new VBox(10);

    public InventoryUI(Player player, Runnable returnToPrevious) {
        this.player = player;
        this.returnToPrevious = returnToPrevious;

        Label titleLabel = new Label("Inventory");
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> returnToPrevious.run());

        HBox header = new HBox(20, titleLabel, healthLabel, backButton);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));

        itemList.setPadding(new Insets(15));
        ScrollPane itemScrollPane = new ScrollPane(itemList);
        itemScrollPane.setFitToWidth(true);

        messageLabel.setPadding(new Insets(10));
        BorderPane.setAlignment(messageLabel, Pos.CENTER);

        setTop(header);
        setCenter(itemScrollPane);
        setBottom(messageLabel);

        refreshInventory();
    }

    private void refreshInventory() {
        healthLabel.setText("HP: " + player.getHealthpoints() + "/" + player.getMaxHealthpoints());
        itemList.getChildren().clear();

        if (player.getInventory().getItems().isEmpty()) {
            itemList.getChildren().add(new Label("Inventory is empty."));
            return;
        }

        for (Item item : player.getInventory().getItems()) {
            itemList.getChildren().add(createItemRow(item));
        }
    }

    private HBox createItemRow(Item item) {
        Label itemLabel = new Label(item.getName());
        Label valueLabel = new Label();
        ImageAssets.setGoldAmount(valueLabel, item.getValue());
        HBox titleRow = new HBox(8, itemLabel, valueLabel);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label descriptionLabel = new Label(buildItemDescription(item));
        descriptionLabel.setWrapText(true);

        VBox itemText = new VBox(4, titleRow, descriptionLabel);
        HBox.setHgrow(itemText, Priority.ALWAYS);

        Button useButton = new Button("Use");
        if (item instanceof HPPot potion) {
            useButton.setOnAction(event -> usePotion(potion));
        } else {
            useButton.setDisable(true);
        }

        HBox itemRow = new HBox(15);
        ImageView itemIcon = ImageAssets.itemIcon(item, 42);
        if (itemIcon != null) {
            itemRow.getChildren().add(itemIcon);
        }
        itemRow.getChildren().addAll(itemText, useButton);
        itemRow.setAlignment(Pos.CENTER_LEFT);
        itemRow.setPadding(new Insets(8));
        return itemRow;
    }

    private String buildItemDescription(Item item) {
        String description = item.getDescription();

        if (item instanceof HPPot potion) {
            return description + " Heal: " + potion.getHealAmount();
        }

        if (item instanceof Weapon weapon) {
            return description + " Damage: " + weapon.getDamage() + " " + weapon.getDamageType();
        }

        return description;
    }

    private void usePotion(HPPot potion) {
        potion.consume(player);
        player.getInventory().removeItem(potion);
        messageLabel.setText("Used " + potion.getName());
        refreshInventory();
    }
}
