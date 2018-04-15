package com.homework.PIM;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fzm
 * @date 2018/3/29
 **/
public interface Collection<E> extends List<E>{
    Collection<E> getNotes();

    Collection<E> getTodos();

    Collection<E> getAppointments();

    Collection<E> getContact();

    Collection<E> getItemsForDate(LocalDate d);

}
