package com.homework.PIM.collection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author fzm
 * @date 2018/3/29
 **/
public class PIMCollectionImp<E> extends ArrayList<E> implements Collection<E> {

    @Override
    public int size() {
        return super.size();
    }


    @Override
    public Collection getNotes() {
        return this.stream()
                .filter((E p) -> "PIMNote".equals(p.getClass().getSimpleName()))
                .collect(Collectors.toCollection(PIMCollectionImp::new));
    }

    @Override
    public Collection getTodos() {
        return this.stream()
                .filter((E p) -> "PIMTodo".equals(p.getClass().getSimpleName()))
                .collect(Collectors.toCollection(PIMCollectionImp::new));
    }

    @Override
    public Collection getAppointments() {
        return this.stream()
                .filter((E p) -> "PIMAppointment".equals(p.getClass().getSimpleName()))
                .collect(Collectors.toCollection(PIMCollectionImp::new));
    }

    @Override
    public Collection getContact() {
        return this.stream()
                .filter((E p) -> "PIMContact".equals(p.getClass().getSimpleName()))
                .collect(Collectors.toCollection(PIMCollectionImp::new));
    }

    @Override
    public Collection getItemsForDate(LocalDate d) {
        return this.stream()
                .filter((E p) -> {
                    try {
                        return p.getClass().getMethod("getDate").invoke(p).toString().equals(d.toString());
                    } catch (Exception e) {
                        return false;
                    }
                }).collect(Collectors.toCollection(PIMCollectionImp::new));
    }

}
