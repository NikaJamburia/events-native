package ge.nika;

public interface EventHandler {
    void handle(Event event);
    String subscribesTo();
}
