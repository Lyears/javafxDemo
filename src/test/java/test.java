import com.homework.PIM.Collection;
import com.homework.PIM.PIMCollection;
import com.homework.PIM.entity.PIMTodo;

import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/3/30
 **/
public class test {
    public static void main(String[] args) {
        Collection<Object> entities = new PIMCollection<>();
        PIMTodo todo = new PIMTodo(LocalDate.now(),"ss");

        entities.add(todo);
        System.out.println(entities.getItemsForDate(LocalDate.now()));

    }
}
