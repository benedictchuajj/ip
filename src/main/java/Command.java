public abstract class Command {
    public boolean isExit() {
        return false;
    }

    public abstract void execute();
}