package Validators;

import util.ReadProperties;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class YValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        Double value = null;
        try {
            value = (Double) o;
        } catch (Exception e){
            throw new ValidatorException(new FacesMessage(ReadProperties.read("ErrorYisNull")));//"Y can't be null or string"
        }
        if (!(value + 5 > 0.00000001 && value - 5 < 0.00000001)){
            throw new ValidatorException(new FacesMessage(ReadProperties.read("ErrorYisInvalid")));//"The value must be in range from -5 to 5"
        }
    }
}
