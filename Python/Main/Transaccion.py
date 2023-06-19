from typing import List
from Divisa import Divisa
from Canal import Canal
from Tarjeta import Tarjeta


class Transaccion:
    transacciones = []  # Lista para almacenar todas las transacciones realizadas

    def __init__(self, cliente_origen, cliente_objetivo, tarjeta_origen, tarjeta_objetivo, cantidad,
                 validez=None, factura=None, mensaje=None, retornable=True, pendiente=False,
                 impuestoRetorno=None, canal=None, rechazado=False, retirar=None):
        """
        Constructor de la clase Transaccion.

        Parámetros:
        - cliente_origen: Cliente que realiza la transacción.
        - cliente_objetivo: Cliente receptor de la transacción.
        - tarjeta_origen: Tarjeta de la que se realiza la transacción.
        - tarjeta_objetivo: Tarjeta receptora de la transacción.
        - cantidad: Cantidad de dinero involucrada en la transacción.
        - validez: Fecha de validez de la transacción.
        - factura: Factura asociada a la transacción.
        - mensaje: Mensaje adicional de la transacción.
        - retornable: Indica si la transacción puede ser deshecha.
        - pendiente: Indica si la transacción está pendiente de completar.
        - impuestoRetorno: Impuesto asociado a la transacción reversa.
        - canal: Canal utilizado para realizar la transacción.
        - rechazado: Indica si la transacción ha sido rechazada.
        - retirar: Indica si la transacción es de retirar o depositar dinero.
        """
        if retirar is not None:  # Condición que se activa únicamente en el contexto de la funcionalidad retirar o depositar dinero
            if retirar:
                self.cliente_origen = cliente_origen
                self.tarjeta_origen = tarjeta_origen
            else:
                self.cliente_objetivo = cliente_origen
                self.tarjeta_objetivo = tarjeta_origen
            self.cantidad = cantidad
            self.impuesto = cantidad * (canal.getImpuesto() / 100)
            self.canal = canal
            self.divisa = tarjeta_origen.getDivisa()
            self.rechazado = rechazado
            retornable = False
            return

        # Asignación de valores a los atributos de la transacción
        self.cliente_origen = cliente_origen
        self.cliente_objetivo = cliente_objetivo
        self.tarjeta_origen = tarjeta_origen
        self.tarjeta_objetivo = tarjeta_objetivo
        self.cantidad = cantidad
        self.validez = validez
        self.factura = factura
        self.mensaje = mensaje
        self.retornable = retornable
        self.pendiente = pendiente
        self.impuestoRetorno = impuestoRetorno
        self.canal = canal
        self.rechazado = rechazado
        self.divisa = tarjeta_origen.getDivisa()

        # Agregar la transacción a la lista de transacciones
        Transaccion.transacciones.append(self)

    # Propiedades para acceder y modificar los atributos privados rechazado, pendiente y retornable
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
        """
        Encuentra todas las transacciones que cumplan con los criterios de búsqueda dados.

        Parámetros:
        - cliente_origen: Cliente origen de las transacciones.
        - divisa: Divisa de las transacciones.

        Retorna:
        - Una lista de transacciones que cumplen con los criterios de búsqueda.
        """
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
        """
        Encuentra todas las transacciones que cumplan con los criterios de búsqueda dados.

        Parámetros:
        - cliente_origen: Cliente origen de las transacciones.
        - cliente_objetivo: Cliente objetivo de las transacciones.

        Retorna:
        - Una lista de transacciones que cumplen con los criterios de búsqueda.
        """
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
        """
        Encuentra todas las transacciones que cumplan con los criterios de búsqueda dados.

        Parámetros:
        - cliente_origen: Cliente origen de las transacciones.
        - tarjeta: Tarjeta origen de las transacciones.

        Retorna:
        - Una lista de transacciones que cumplen con los criterios de búsqueda.
        """
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
        """
        Encuentra todas las transacciones que cumplan con los criterios de búsqueda dados.

        Parámetros:
        - cliente_origen: Cliente origen de las transacciones.

        Retorna:
        - Una lista de transacciones que cumplen con los criterios de búsqueda.
        """
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
        """
        Retorna todas las transacciones registradas.

        Retorna:
        - Una lista con todas las transacciones registradas.
        """
        return Transaccion.transacciones

    def getClienteOrigen(self):
        """
        Retorna el cliente origen de la transacción.

        Retorna:
        - El cliente origen de la transacción.
        """
        return self.cliente_origen

    def getClienteObjetivo(self):
        """
        Retorna el cliente objetivo de la transacción.

        Retorna:
        - El cliente objetivo de la transacción.
        """
        return self.cliente_objetivo

    def getTarjetaOrigen(self):
        """
        Retorna la tarjeta origen de la transacción.

        Retorna:
        - La tarjeta origen de la transacción.
        """
        return self.tarjeta_origen

    def getMensaje(self):
        """
        Retorna el mensaje de la transacción.

        Retorna:
        - El mensaje de la transacción.
        """
        return self.mensaje

    def isPendiente(self):
        """
        Indica si la transacción está pendiente.

        Retorna:
        - True si la transacción está pendiente, False en caso contrario.
        """
        return self.pendiente

    def __str__(self):
        if not hasattr(self, "mensaje"):
            if hasattr(self, "cliente_origen"):
                return "Operación de retiro por " + str(self.cantidad)
            else:
                return "Operación de depósito por " + str(self.cantidad)
        if self.mensaje and self.pendiente:
            return f"El cliente {self.cliente_origen.getNombre()} quisiera deshacer una transacción por {(self.cantidad)} {self.tarjeta_origen.getDivisa().name} recibidos por la tarjeta #{self.tarjeta_objetivo.getNoTarjeta()}\nSu mensaje es: {self.mensaje}"
        if self.rechazado:
            if not self.tarjeta_objetivo:
                return f"Transacción Rechazada\nTotal: {self.cantidad} {self.tarjeta_origen.getDivisa().name}\nTarjeta: #{self.tarjeta_origen.getNoTarjeta()}"
            if not self.tarjeta_origen:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa().name}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
            if not self.cliente_origen:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}"
            else:
                return f"Transacción Rechazada\nTotal: {(self.cantidad)} {self.tarjeta_objetivo.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}\nProveniente de: {self.cliente_origen.getNombre()}\n"
        elif self.pendiente:
            return f"Transacción Pendiente\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}\nProveniente de: {self.cliente_origen.getNombre()}"
        elif self.validez:
            return (
                f"Transacción Aprobada\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}\nProveniente de: {self.cliente_origen.getNombre()}\nValidez: {self.validez}\nMensaje: {self.mensaje}"
            )
        else:
            return f"Transacción Realizada\nTotal: {(self.cantidad)} {self.tarjeta_origen.getDivisa().name}\nTarjeta de origen: #{self.tarjeta_origen.getNoTarjeta()}\nTarjeta de destino: #{self.tarjeta_objetivo.getNoTarjeta()}\nProveniente de: {self.cliente_origen.getNombre()}\nMensaje: {self.mensaje}"

