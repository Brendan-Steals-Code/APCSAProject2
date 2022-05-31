package jade.fromTutorial;

public abstract class Component {

    public GameObject gameObject = null;

    public void start() {

    }

    public void destroy() {

    }

    public abstract void update(float dt);
}