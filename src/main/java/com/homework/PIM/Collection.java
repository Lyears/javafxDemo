package com.homework.PIM;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fzm
 * @date 2018/3/29
 **/
public interface Collection<E> extends List<E>{
    Collection getNotes();

    Collection getTodos();

    Collection getAppointments();

    Collection getContact();

    Collection getItemsForDate(LocalDate d);

}
