
package com.digis01.HAriasProgramacionNCapasClientJPACajero.ML;

import java.util.List;


public class Result<T> {

    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public T object;
    public List<T> objects;
}
