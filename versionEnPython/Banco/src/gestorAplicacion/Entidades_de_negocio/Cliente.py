from Banco.src.gestorAplicacion.Entidades_de_negocio.Transaccion import Transaccion


class Cliente:
    def __init__(self, nombre, noDeIdentificacion):
        """
        Constructor de la clase Cliente.

        Args:
            nombre (str): Nombre del cliente.
            noDeIdentificacion (int): Número de identificación del cliente.
        """
        from Banco import Banco
        self.nombre = nombre
        self.Id = noDeIdentificacion
        self.tarjetasDebito = []
        self.tarjetasCredito = []
        self.facturas = []
        Banco.agregarCliente(self)

    def getNombre(self):
        """
        Devuelve el nombre del cliente.

        Returns:
            str: Nombre del cliente.
        """
        return self.nombre

    def revisarHistorialDeCreditos(self):
        """
        Revisa el historial de transacciones de crédito del cliente.

        Returns:
            list: Lista de transacciones de crédito realizadas por el cliente.
        """
        from Banco.src.gestorAplicacion.tarjetas.TarjetaCredito import TarjetaCredito
        retorno = []
        for t in Transaccion.getTransacciones():
            if t.getClienteOrigen() == self and not t.rechazado and isinstance(t.getTarjetaOrigen(),TarjetaCredito) and not t.pendiente:
                retorno.append(t)
        return retorno

    def getTarjetasCredito(self):
        """
        Devuelve las tarjetas de crédito del cliente.

        Returns:
            list: Lista de tarjetas de crédito del cliente.
        """
        return self.tarjetasCredito

    def getTarjetasDebito(self):
        """
        Devuelve las tarjetas de débito del cliente.

        Returns:
            list: Lista de tarjetas de débito del cliente.
        """
        return self.tarjetasDebito

    def getFacturas(self):
        """
        Devuelve las facturas del cliente.

        Returns:
            list: Lista de facturas del cliente.
        """
        return self.facturas

    def agregarFactura(self, f):
        """
        Agrega una factura a la lista de facturas del cliente.

        Args:
            f (Factura): Factura a agregar.
        """
        self.facturas.append(f)

    def getTarjetas(self):
        """
        Devuelve todas las tarjetas del cliente.

        Returns:
            list: Lista de todas las tarjetas del cliente.
        """
        return self.tarjetasCredito + self.tarjetasDebito

    def listarTarjetas(self, factura):
        """
        Lista las tarjetas disponibles para pagar una factura en una divisa determinada.

        Args:
            factura (Factura): Factura a pagar.

        Returns:
            list: Lista de tarjetas disponibles para pagar la factura.
        """
        tarjetasDisponibles = []
        for tarjeta in self.tarjetasDebito:
            if not tarjeta.tieneSaldo():
                continue
            if tarjeta.getDivisa() == factura.getDIVISA():
                tarjetasDisponibles.append(tarjeta)
        for tarjeta in self.tarjetasCredito:
            if not tarjeta.tieneSaldo():
                continue
            if tarjeta.getDivisa() == factura.getDIVISA():
                tarjetasDisponibles.append(tarjeta)
        return tarjetasDisponibles

    def agregarTarjetasDebito(self, *tarjetasDebito):
        """
        Agrega una o varias tarjetas de débito al cliente.

        Args:
            *tarjetasDebito: Tarjetas de débito a agregar.
        """
        self.tarjetasDebito.extend(list(tarjetasDebito))

    def agregarTarjetasCredito(self, *tarjetasCredito):
        """
        Agrega una o varias tarjetas de crédito al cliente.

        Args:
            *tarjetasCredito: Tarjetas de crédito a agregar.
        """
        self.tarjetasCredito.extend(list(tarjetasCredito))

    def encontrarTarjeta(self, tarjeta):
        """
        Encuentra una tarjeta basándose en su representación en cadena (toString).

        Args:
            tarjeta: Tarjeta a buscar.

        Returns:
            Tarjeta: Tarjeta encontrada o None si no se encuentra.
        """
        for t in self.tarjetasCredito:
            if t.__str__() == tarjeta.__str__():
                return t
        for t in self.tarjetasDebito:
            if t.__str__() == tarjeta.__str__():
                return t

    def encontrarFacturas(self, factura):
        """
        Encuentra una factura basándose en su representación en cadena (toString).

        Args:
            factura: Factura a buscar.

        Returns:
            Factura: Factura encontrada o None si no se encuentra.
        """
        for f in self.facturas:
            if f.__str__() == factura.__str__():
                return f

    def mostrarFacturas(self):
        """
        Muestra las facturas pendientes de pago del cliente.

        Returns:
            list: Lista de facturas pendientes de pago.
        """
        retorno = []
        for f in self.facturas:
            if not f.facturaPagada:
                retorno.append(f)
        return retorno

    def verPeticiones(self):
        """
        Muestra las transacciones pendientes del cliente que tienen un mensaje adjunto.

        Returns:
            list: Lista de transacciones pendientes con mensaje.
        """
        retorno = []
        for t in Transaccion.getTransacciones():
            if t.getClienteObjetivo() is not None:
                if t.getClienteObjetivo() == self and t.getMensaje() is not None and t.isPendiente():
                    retorno.append(t)
        return retorno

    def tarjetasConDivisa(self, divisa, origen: bool):
        """
        Devuelve una lista de tarjetas del cliente que tienen una divisa determinada.

        Args:
            divisa (str): Divisa a buscar.
            origen (bool): Indica si se deben incluir las tarjetas de crédito (True) o no (False).

        Returns:
            list: Lista de tarjetas con la divisa especificada.
        """
        retorno = []
        for t in self.tarjetasDebito:
            if not t.isActiva():
                continue
            if not t.tieneSaldo():
                continue
            if t.getDivisa() == divisa:
                retorno.append(t)
        if origen:
            for t in self.tarjetasCredito:
                if not t.isActiva():
                    continue
                if not t.tieneSaldo():
                    continue
                if t.getDivisa() == divisa:
                    retorno.append(t)
        return retorno

    def listarCanales(self, divisao, divisad):
        """
        Lista los canales disponibles para realizar una transacción entre dos divisas.

        Args:
            divisao (str): Divisa de origen.
            divisad (str): Divisa de destino.

        Returns:
            list: Lista de canales disponibles ordenados por impuestos.
        """
        from Banco import Banco
        divisaOrigen = divisao
        divisaDestino = divisad
        canales = []
        for canal in Banco.getCanales():
            if canal.tieneDivisa(divisaOrigen):
                continue
            if canal.tieneDivisa(divisaDestino):
                continue
            canales.append(canal)
        return Banco.ordenarCanalesPorImpuestos(canales)

    def seleccionarTarjeta(self, divisa, retirar):
        """
        Selecciona las tarjetas disponibles para realizar una transacción en una divisa determinada.

        Args:
            divisa (str): Divisa de la transacción.
            retirar (bool): Indica si se trata de una transacción de retiro (True) o no (False).

        Returns:
            list: Lista de tarjetas disponibles para realizar la transacción.
        """
        retorno = []
        for t in self.tarjetasDebito:
            if t.divisa == divisa:
                retorno.append(t)
        if not retirar:
            for t in self.tarjetasCredito:
                if t.divisa == divisa:
                    retorno.append(t)
        return retorno

