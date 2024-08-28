package com.workeache.precionline.api.demo.utils.notifications;

public class Constant {
    public static final Message A001_SUCCESS = new Message("Actualización A001","Precios día actual actualizados desde endpoint",1);
    public static final Message A002_SUCCESS = new Message("Actualización A002","Precios día siguiente actualizados desde endpoint",1);
    public static final Message A003_SUCCESS = new Message("Actualización A003","Precios día siguiente actualizados automáticamente",1);

    public static final Message A001_ERROR = new Message("Error Act. A001","Error al actualizar los precios del día actual desde endpoint",1);
    public static final Message A002_ERROR = new Message("Error Act. A002","Error al actualizar los precios del día actual desde endpoint",1);
    public static final Message A003_ERROR = new Message("Error Act. A003","Error al actualizar los precios del día actual automáticamente",1);
    public static final Message A004_ERROR = new Message("Error Act. A004","Error al actualizar los precios del día actual automáticamente. Datos de precios" +
            " no disponles en este momento",1);
}
