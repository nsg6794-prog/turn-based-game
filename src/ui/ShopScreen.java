package ui;

import game.EncounterManager;
import game.Player;
import items.Item;
import items.Shop;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ShopScreen extends BorderPane {
    private final Player player;
    private final Shop shop;
    private final Stage stage;
    private final EncounterManager encounterManager;
    private final Runnable returnToPrevious;
    private final Label goldLabel = new Label();
    private final Label messageLabel = new Label();
    private final VBox stockList = new VBox(10);

    public ShopScreen(Player player, Shop shop, Stage stage, EncounterManager encounterManager) {
        this(player, shop, stage, encounterManager, createSceneReturn(stage));
    }

    public ShopScreen(Player player,
                      Shop shop,
                      Stage stage,
                      EncounterManager encounterManager,
                      Runnable returnToPrevious) {
        this.player = player;
        this.shop = shop;
        this.stage = stage;
        this.encounterManager = encounterManager;
        this.returnToPrevious = returnToPrevious;

        Label titleLabel = new Label("Shop");
        Button leaveShopButton = new Button("Leave Shop");
        leaveShopButton.setOnAction(event -> leaveShop());

        HBox header = new HBox(20, titleLabel, goldLabel, leaveShopButton);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));

        stockList.setPadding(new Insets(15));
        ScrollPane stockScrollPane = new ScrollPane(stockList);
        stockScrollPane.setFitToWidth(true);

        messageLabel.setPadding(new Insets(10));
        BorderPane.setAlignment(messageLabel, Pos.CENTER);

        setTop(header);
        setCenter(stockScrollPane);
        setBottom(messageLabel);

        refreshShop();
    }

    private void refreshShop() {
        ImageAssets.setGoldAmount(goldLabel, "Gold: ", player.getGold());
        stockList.getChildren().clear();

        if (shop.getStock().isEmpty()) {
            stockList.getChildren().add(new Label("No items available."));
            return;
        }

        for (Item item : shop.getStock()) {
            stockList.getChildren().add(createItemRow(item));
        }
    }

    private HBox createItemRow(Item item) {
        Label itemLabel = new Label(item.getName());
        Label priceLabel = new Label();
        ImageAssets.setGoldAmount(priceLabel, item.getValue());
        HBox titleRow = new HBox(8, itemLabel, priceLabel);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label descriptionLabel = new Label(item.getDescription());
        descriptionLabel.setWrapText(true);

        VBox itemText = new VBox(4, titleRow, descriptionLabel);
        HBox.setHgrow(itemText, Priority.ALWAYS);

        Button buyButton = new Button("Buy");
        buyButton.setOnAction(event -> buyItem(item));

        HBox itemRow = new HBox(15);
        ImageView itemIcon = ImageAssets.itemIcon(item, 42);
        if (itemIcon != null) {
            itemRow.getChildren().add(itemIcon);
        }
        itemRow.getChildren().addAll(itemText, buyButton);
        itemRow.setAlignment(Pos.CENTER_LEFT);
        itemRow.setPadding(new Insets(8));
        return itemRow;
    }

    private void buyItem(Item item) {
        if (shop.buyItem(player, item)) {
            messageLabel.setText("");
            refreshShop();
        } else {
            messageLabel.setText("Not enough gold");
            ImageAssets.setGoldAmount(goldLabel, "Gold: ", player.getGold());
        }
    }

    private void leaveShop() {
        returnToPrevious.run();
    }

    private static Runnable createSceneReturn(Stage stage) {
        Scene previousScene = stage.getScene();
        return () -> {
            if (previousScene != null) {
                stage.setScene(previousScene);
            } else {
                GameApplication.showMainMenu(stage);
            }
        };
    }
}
