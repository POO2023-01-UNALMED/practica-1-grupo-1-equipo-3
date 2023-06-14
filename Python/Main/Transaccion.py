
class Transaccion:
    transacciones = []

    def _init_(self, cliente_objetivo, cliente_origen, tarjeta_origen, tarjeta_objetivo, cantidad):
        self.cliente_objetivo = cliente_objetivo
        self.cliente_origen = cliente_origen
        self.tarjeta_origen = tarjeta_origen
        self.tarjeta_objetivo = tarjeta_objetivo
        self.cantidad = cantidad
        self.rechazado = not tarjeta_origen.transaccion(cantidad, tarjeta_objetivo)
        self.pendiente = False
        self.retornable = True
        self.divisa = tarjeta_origen.get_divisa()
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

    @property
    def divisa(self):
        return self._divisa

    @divisa.setter
    def divisa(self, value):
        self._divisa = value

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


    def _str_(self):
        from Banco import Banco #Es necesario importar de esta manera, de lo contrario, causaría un error por import circular
        if self.rechazado:
            if self.tarjeta_objetivo is None:
                return (
                    "Transacción Rechazada\n"
                    "Total: "
                    + Banco.formatear_numero(self.cantidad)
                    + " "
                    + self.tarjeta_origen.get_divisa()
                    + "\n"
                    "Tarjeta: #"
                    + self.tarjeta_origen.get_no_tarjeta()
                )
            if self.tarjeta_origen is None:
                return (
                    "Transacción Rechazada\n"
                    "Total: "
                    + Banco.formatear_numero(self.cantidad)
                    + " "
                    + self.tarjeta_objetivo.get_divisa()
                    + "\n"
                    "Tarjeta: #"
                    + self.tarjeta_objetivo.get_no_tarjeta()
                )
            return "Transacción Rechazada"
        if self.tarjeta_origen is None:
            return (
                "Transacción Realizada\n"
                "Total: "
                + Banco.formatear_numero(self.cantidad)
                + " "
                + self.tarjeta_objetivo.get_divisa()
                + "\n"
                "Tarjeta: #"
                + self.tarjeta_objetivo.get_no_tarjeta()
            )
        if self.tarjeta_objetivo is None:
            return (
                "Transacción Realizada\n"
                "Total: "
                + Banco.formatear_numero(self.cantidad)
                + " "
                + self.tarjeta_origen.get_divisa()
                + "\n"
                "Tarjeta: #"
                + self.tarjeta_origen.get_no_tarjeta()
            )
        return "Transacción Realizada"
    

    def _repr_(self):
        return self._str_()