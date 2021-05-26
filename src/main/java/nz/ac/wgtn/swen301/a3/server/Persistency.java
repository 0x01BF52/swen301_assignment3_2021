package nz.ac.wgtn.swen301.a3.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Persistency {
    private final List<LogEvent> list;

    public Persistency() {
        this.list = new CopyOnWriteArrayList<>();
    }

    public Boolean add(LogEvent s) {
        return this.list.add(s);
    }


    public int getSize() {
        return list.size();
    }


    public List<LogEvent> getList() {
        return Collections.unmodifiableList(list);
    }
}
