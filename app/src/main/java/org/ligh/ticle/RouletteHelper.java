package org.ligh.ticle;

public class RouletteHelper {
    String result;
    String target;
    boolean isSuccess;
    boolean isSelected;

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public boolean getSelected() {
        return isSelected;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public void setIsSuccess(String point) {
        this.result = result;
        isSuccess = point.equals(target);
    }
    public boolean getIsSuccess() {
        return isSuccess;
    }
}
