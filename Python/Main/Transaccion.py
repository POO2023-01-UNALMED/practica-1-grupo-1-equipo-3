
class Transaccion:
    transacciones = []

    def __init__(self, cliente_origen, cliente_objetivo, tarjeta_origen, tarjeta_objetivo, cantidad, validez = None, factura = None, mensaje = None):
        self.cliente_objetivo = cliente_objetivo
        self.cliente_origen = cliente_origen
        self.tarjeta_origen = tarjeta_origen
        self.tarjeta_objetivo = tarjeta_objetivo
        self.cantidad = cantidad
        self.rechazado = not tarjeta_origen.transaccion(cantidad, tarjeta_objetivo)
        self.pendiente = False
        self.retornable = True
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
    
    @staticmethod
    def getTransacciones():
        return Transaccion.transacciones
    
    def getClienteOrigen(self):
        return self.cliente_origen
    
    def getTarjetaOrigen(self):
        return self.tarjeta_origen


    def __str__(self):
        if self.mensaje and self.pendiente:
            return f"El cliente {self.cliente_origen.getNombre()} quisiera deshacer una transacción por {(self.cantidad)} {self.tarjeta_origen.getDivisa()} recibidos por la tarjeta #{self.tarjeta_objetivo.getNoTarjeta()}\nSu mensaje es: {self.mensaje}"
        if self.rechazado:
            if not self.tarjeta_objetivo:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa()}\nTarjeta: #{self.tarjeta_origen.getNoTarjeta()}"
            if not self.tarjeta_origen:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
            if not self.cliente_origen:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa()}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
            else:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa()}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}\nProveniente de: {self.cliente_origen.getNombre()}\n"
        elif self.pendiente:
            return f"Transacción Pendiente\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa()}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
        else:
            return f"Transacción Completada\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa()}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"

    

    def _repr_(self):
        return self._str_()