
class Transaccion:
    transacciones = []

    def __init__(self, cliente_origen, cliente_objetivo, tarjeta_origen, tarjeta_objetivo, cantidad, validez = None, factura = None, mensaje = None, retornable = True, pendiente = False):
        self.cliente_objetivo = cliente_objetivo
        self.cliente_origen = cliente_origen
        self.tarjeta_origen = tarjeta_origen
        self.tarjeta_objetivo = tarjeta_objetivo
        self.cantidad = cantidad
        if not pendiente:
            self.rechazado = not tarjeta_origen.transaccion(cantidad, tarjeta_objetivo)
        else:
            self.rechazado = False
        self.pendiente = pendiente
        self.retornable = retornable
        self.validez = validez
        self.factura = factura
        self.mensaje = mensaje
        self.divisa = tarjeta_origen.getDivisa()
        Transaccion.transacciones.append(self)

    @property
    def rechazado(self):
        return self._rechazado

    @rechazado.setter
    def rechazado(self, value):
        self._rechazado = value

    @property
    def pendiente(self):
        return self._pendiente

    @pendiente.setter
    def pendiente(self, value):
        self._pendiente = value

    @property
    def retornable(self):
        return self._retornable

    @retornable.setter
    def retornable(self, value):
        self._retornable = value

    def getDivisa(self):
        return self.divisa
    
    def getCantidad(self):
        return self.cantidad

    def setRetornable(self, ret):
        self.retornable = ret

    @staticmethod
    def encontrar_transacciones(cliente_origen, divisa):
        retorno = []
        for t in Transaccion.transacciones:
            if (
                t.divisa == divisa
                and t.cliente_origen == cliente_origen
                and t.cliente_objetivo is not None
                and t.retornable
                and not t.pendiente
                and not t.rechazado
            ):
                retorno.append(t)
        return retorno

    @staticmethod
    def encontrar_transacciones(cliente_origen, cliente_objetivo):
        retorno = []
        for t in Transaccion.transacciones:
            if (
                t.cliente_origen == cliente_origen
                and t.cliente_objetivo == cliente_objetivo
                and t.retornable
                and not t.pendiente
                and not t.rechazado
            ):
                retorno.append(t)
        return retorno

    @staticmethod
    def encontrar_transacciones(cliente_origen, tarjeta):
        retorno = []
        for t in Transaccion.transacciones:
            if (
                t.cliente_origen == cliente_origen
                and t.tarjeta_origen == tarjeta
                and t.cliente_objetivo is not None
                and t.retornable
                and not t.pendiente
                and not t.rechazado
            ):
                retorno.append(t)
        return retorno
    
    def encontrar_transacciones(cliente_origen):
        retorno = []
        for t in Transaccion.transacciones:
            print(t)
            if (
                t.cliente_origen == cliente_origen
                and t.cliente_objetivo is not None
                and t.retornable
                and not t.pendiente
                and not t.rechazado
            ):
                retorno.append(t)
        return retorno
    
    @staticmethod
    def getTransacciones():
        return Transaccion.transacciones
    
    def getClienteOrigen(self):
        return self.cliente_origen
    
    def getClienteObjetivo(self):
        return self.cliente_objetivo

    def getTarjetaOrigen(self):
        return self.tarjeta_origen

    def getMensaje(self):
        return self.mensaje
    
    def isPendiente(self):
        return self.pendiente

    def __str__(self):
        if self.mensaje and self.pendiente:
            return f"El cliente {self.cliente_origen.getNombre()} quisiera deshacer una transacción por {(self.cantidad)} {self.tarjeta_origen.getDivisa().name} recibidos por la tarjeta #{self.tarjeta_objetivo.getNoTarjeta()}\nSu mensaje es: {self.mensaje}"
        if self.rechazado:
            if not self.tarjeta_objetivo:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa().name}\nTarjeta: #{self.tarjeta_origen.getNoTarjeta()}"
            if not self.tarjeta_origen:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa().name}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
            if not self.cliente_origen:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
            else:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}\nProveniente de: {self.cliente_origen.getNombre()}\n"
        elif self.pendiente:
            return f"Transacción Pendiente\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
        else:
            return f"Transacción Completada\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"

    def _repr_(self):
        return self._str_()
    
    def encontrarTransaccion(transaccion):
        for t in Transaccion.transacciones:
            if t.retornable and t.__str__() == transaccion:
                return t
    
    @staticmethod
    def completarTransaccion(transaccion, respuesta):
        if not respuesta:
            transaccion.rechazado = True
            transaccion.pendiente = False
        else:
            transaccion.rechazado = False
            transaccion.pendiente = False
            transaccion.tarjeta_objetivo.deshacerTransaccion(transaccion.cantidad, transaccion.tarjeta_origen)
        return transaccion
