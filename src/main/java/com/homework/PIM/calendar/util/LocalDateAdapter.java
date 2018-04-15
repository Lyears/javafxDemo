package com.homework.PIM.calendar.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/4/15
 **/
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {


    @Override
    public LocalDate unmarshal(String v) {
        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) {
        return v.toString();
    }
}
