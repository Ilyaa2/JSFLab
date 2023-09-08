package Validators;

import util.ReadProperties;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Arrays;

public class RValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        double[] array = {1, 1.5, 2, 2.5, 3, 3.5, 4};
        Double value = null;
        try{
            value = (Double) o;
            ((Double) o).intValue();
        } catch (Exception e){
            throw new ValidatorException(new FacesMessage(ReadProperties.read("ErrorRisNull"))); //"R can't be null or string"
        }
        if (value< 0.00000001){
            throw new ValidatorException(new FacesMessage(ReadProperties.read("ErrorRis0")));//"R can't be 0"
        }
        if (Arrays.binarySearch(array, value) < 0){
            throw new ValidatorException(new FacesMessage(ReadProperties.read("ErrorIsInvalid"))); //"R must be equal to one of these values in the slider"
        }
    }
}
