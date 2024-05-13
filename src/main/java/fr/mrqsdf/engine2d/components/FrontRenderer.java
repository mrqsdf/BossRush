package fr.mrqsdf.engine2d.components;

public class FrontRenderer extends Component {

    @Override
    public void start(){
        if (gameObject.getComponent(SpriteRenderer.class) != null){
            System.out.println("found front Renderer");
        }
    }

    @Override
    public void update(float dt) {

    }
}
