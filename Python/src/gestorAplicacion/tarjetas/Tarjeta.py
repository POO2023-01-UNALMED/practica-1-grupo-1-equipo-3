from abc import ABC, abstractmethod
from BorrarTarjeta import BorrarTarjeta
import pickle


class Tarjeta(ABC):
    tarjetas = []

    def __init__(self, noTarjeta, divisa):
        """
        Crea una instancia de Tarjeta.

        Args:
            noTarjeta (str): Número de la tarjeta.
            divisa (Divisa): Divisa asociada a la tarjeta.

        """
        self.noTarjeta = noTarjeta
        self.divisa = divisa
        self.estado = "ACTIVA"
        self.transaccionesRechazadas = 0
        self.erroresActuales = 0
        Tarjeta.tarjetas.append(self)

    def getNoTarjeta(self):
        """
        Obtiene el número de tarjeta.

        Returns:
            str: Número de tarjeta.

        """
        return self.noTarjeta

    def getDivisa(self):
        """
        Obtiene la divisa asociada a la tarjeta.

        Returns:
            Divisa: Divisa asociada.

        """
        return self.divisa

    def anadirTransaccionRechazada(self):
        """
        Aumenta el contador de transacciones rechazadas de la tarjeta.

        Si se alcanzan 3 transacciones rechazadas, se bloquea la tarjeta.

        """
        if self.transaccionesRechazadas >= 3:
            self.estado = "BLOQUEADA"
        self.transaccionesRechazadas += 1

    def getEstado(self):
        """
        Obtiene el estado de la tarjeta.

        Returns:
            str: Estado de la tarjeta ("ACTIVA" o "BLOQUEADA").

        """
        return self.estado

    def isActiva(self):
        """
        Verifica si la tarjeta está activa.

        Returns:
            bool: True si la tarjeta está activa, False de lo contrario.

        """
        return self.getEstado() == "ACTIVA"

    def tarjetaABorrar(self):
        """
        Verifica si la tarjeta debe ser borrada debido a errores.

        Returns:
            bool: True si la tarjeta debe ser borrada, False de lo contrario.

        """
        return self.erroresActuales > BorrarTarjeta.erroresMax

    def setErroresActuales(self, errores):
        """
        Establece la cantidad actual de errores de la tarjeta.

        Args:
            errores (int): Cantidad de errores.

        """
        self.erroresActuales = errores

    def anadirError(self):
        """
        Aumenta la cantidad de errores de la tarjeta en uno.

        """
        self.erroresActuales += 1

    @staticmethod
    def getTarjetas():
        """
        Obtiene todas las tarjetas existentes.

        Returns:
            List[Tarjeta]: Lista de tarjetas.

        """
        return Tarjeta.tarjetas

    @abstractmethod
    def transaccion(self, cantidad, t):
        """
        Realiza una transacción entre la tarjeta actual y otra tarjeta.

        Args:
            cantidad (float): Monto de la transacción.
            t (Tarjeta): Tarjeta con la que se realiza la transacción.

        Returns:
            bool: True si la transacción se realizó con éxito, False de lo contrario.

        """
        pass

    @abstractmethod
    def deshacerTransaccion(self, cantidad, t):
        """
        Deshace una transacción realizada entre la tarjeta actual y otra tarjeta.

        Args:
            cantidad (float): Monto de la transacción a deshacer.
            t (Tarjeta): Tarjeta con la que se realizó la transacción.

        """
        pass

    @abstractmethod
    def tieneSaldo(self):
        """
        Verifica si la tarjeta tiene saldo suficiente para realizar una transacción.

        Returns:
            bool: True si la tarjeta tiene saldo suficiente, False de lo contrario.

        """
        pass

    @abstractmethod
    def puedeTransferir(self, monto):
        """
        Verifica si la tarjeta puede transferir un monto determinado.

        Args:
            monto (float): Monto a transferir.

        Returns:
            bool: True si la tarjeta puede transferir el monto, False de lo contrario.

        """
        pass

    @abstractmethod
    def sacarDinero(self, monto):
        """
        Realiza una transacción para sacar dinero de la tarjeta.

        Args:
            monto (float): Monto a sacar.

        """
        pass

    @abstractmethod
    def introducirDinero(self, monto):
        """
        Realiza una transacción para introducir dinero en la tarjeta.

        Args:
            monto (float): Monto a introducir.

        """
        pass

    @staticmethod
    def TarjetasBloqueadas(cliente):
        """
        Obtiene todas las tarjetas bloqueadas de un cliente.

        Args:
            cliente (Cliente): Cliente del cual se obtienen las tarjetas bloqueadas.

        Returns:
            List[Tarjeta]: Lista de tarjetas bloqueadas.

        """
        retorno = []
        for t in cliente.getTarjetasCredito():
            if not t.isActiva():
                retorno.append(t)
        for t in cliente.getTarjetasDebito():
            if not t.isActiva():
                retorno.append(t)
        return retorno

    @staticmethod
    def TarjetasNoBloqueadas(cliente):
        """
        Obtiene todas las tarjetas no bloqueadas de un cliente.

        Args:
            cliente (Cliente): Cliente del cual se obtienen las tarjetas no bloqueadas.

        Returns:
            List[Tarjeta]: Lista de tarjetas no bloqueadas.

        """
        retorno = []
        for t in cliente.getTarjetasCredito():
            if t.isActiva():
                retorno.append(t)
        for t in cliente.getTarjetasDebito():
            if t.isActiva():
                retorno.append(t)
        return retorno


class TarjetaDebito(Tarjeta):
    def _init_(self, noTarjeta, divisa, saldo):
        """
        Crea una instancia de TarjetaDebito.

        Args:
            noTarjeta (str): Número de la tarjeta.
            divisa (Divisa): Divisa asociada a la tarjeta.
            saldo (float): Saldo inicial de la tarjeta.

        """
        super()._init_(noTarjeta, divisa)
        self.saldo = saldo

    def getSaldo(self):
        """
        Obtiene el saldo actual de la tarjeta.

        Returns:
            float: Saldo de la tarjeta.

        """
        return self.saldo

    def setSaldo(self, saldo):
        """
        Establece el saldo de la tarjeta.

        Args:
            saldo (float): Saldo a establecer.

        """
        self.saldo = saldo

    def _str_(self):
        """
        Devuelve una representación en forma de cadena de la tarjeta.

        Returns:
            str: Representación de la tarjeta.

        """
        from Banco import Banco
        return f"Tipo de tarjeta: Débito\nNúmero de tarjeta: {self.getNoTarjeta()}\nSaldo: {Banco.formatearNumero(self.saldo)} {self.divisa.name()}"

    def transaccion(self, cantidad, tarjeta):
        """
        Realiza una transacción entre la tarjeta de débito actual y otra tarjeta.

        Args:
            cantidad (float): Monto de la transacción.
            tarjeta (Tarjeta): Tarjeta con la que se realiza la transacción.

        Returns:
            bool: True si la transacción se realizó con éxito, False de lo contrario.

        """
        if self.saldo >= cantidad and tarjeta.getDivisa() == self.getDivisa():
            self.saldo -= cantidad
            tarjeta.setSaldo(tarjeta.getSaldo() + cantidad)
            return True
        else:
            return False

    def borrar(self):
        """
        Borra la tarjeta de débito.

        Returns:
            str: Mensaje indicando que la tarjeta será borrada.

        """
        from Banco import Banco
        for c in Banco.getClientes():
            c.getTarjetasDebito().remove(self)
        return f"La tarjeta de crédito #{self.noTarjeta} será borrada, ya que tiene demasiadas transacciones rechazadas"

    def deshacerTransaccion(self, cantidad, t):
        """
        Deshace una transacción realizada entre la tarjeta de débito actual y otra tarjeta.

        Args:
            cantidad (float): Monto de la transacción a deshacer.
            t (Tarjeta): Tarjeta con la que se realizó la transacción.

        """
        if self.saldo >= cantidad and isinstance(t, TarjetaDebito):
            self.saldo -= round(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            t.setSaldo(t.getSaldo() + cantidad)

    def getDivisa(self):
        """
        Obtiene la divisa asociada a la tarjeta de débito.

        Returns:
            Divisa: Divisa asociada.

        """
        return self.divisa
