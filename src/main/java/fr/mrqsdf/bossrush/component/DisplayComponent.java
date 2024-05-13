package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.engine2d.components.Component;

public class DisplayComponent extends Component {

    public DisplayState displayState;

    public DisplayComponent(DisplayState displayState){
        this.displayState = displayState;
    }

}
