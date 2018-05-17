import com.homework.PIM.Collection;
import com.homework.PIM.PIMCollection;
import com.homework.PIM.entity.PIMTodo;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/3/30
 **/
public class test extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane paneDialog = new StackPane();

        JFXDialog dialog = new JFXDialog();
        dialog.setDialogContainer(paneDialog);
        dialog.setOverlayClose(false);
        dialog.setContent(new StackPane(new JFXSpinner()));

        final Scene scene = new Scene(paneDialog, 300, 300);
        stage.setScene(scene);
        stage.setTitle("JFX Spinner Demo");
        dialog.show();
        stage.show();

    }
}
