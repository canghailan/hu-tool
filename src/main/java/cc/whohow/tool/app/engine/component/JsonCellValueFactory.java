package cc.whohow.tool.app.engine.component;

import cc.whohow.tool.app.engine.ImmutableObservableJsonValue;
import cc.whohow.tool.json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class JsonCellValueFactory implements Callback<TableColumn.CellDataFeatures<JsonNode, JsonNode>, ObservableValue<JsonNode>> {
    private final String expression;

    public JsonCellValueFactory(String expression) {
        this.expression = expression;
    }

    @Override
    public ObservableValue<JsonNode> call(TableColumn.CellDataFeatures<JsonNode, JsonNode> cellDataFeatures) {
        return new ImmutableObservableJsonValue(Json.evaluate(cellDataFeatures.getValue(), expression));
    }
}
