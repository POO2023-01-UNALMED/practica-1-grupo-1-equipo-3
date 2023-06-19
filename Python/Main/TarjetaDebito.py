from typing import List
from random import randint
from Tarjeta import Tarjeta
from Divisa import Divisa
import math


class TarjetaDebito(Tarjeta):
    serialVersionUID = 1

    def __init__(self, noTarjeta: int, divisa: Divisa, saldo: float):
        super().__init__(noTarjeta, divisa)
        self.saldo = saldo

    def __str__(self):
        from Banco import Banco
        return "Tipo de tarjeta: Débito\nNúmero de tarjeta: {}\nSaldo: {} {}\n".format(
            self.noTarjeta, math.trunc(self.saldo), self.divisa.name
        )

    def transaccion(self, cantidad: float, tarjeta: "TarjetaDebito") -> bool:
        """
        Realiza una transacción desde la tarjeta de débito actual a otra tarjeta de débito.

        Args:
            cantidad (float): Cantidad a transferir.
            tarjeta (TarjetaDebito): Tarjeta de débito de destino.

        Returns:
            bool: True si la transacción fue exitosa, False en caso contrario.
        """
        if self.saldo >= cantidad and tarjeta.getDivisa() == self.getDivisa():
            self.saldo -= cantidad
            tarjeta.setSaldo(tarjeta.getSaldo() + cantidad)
            return True
        else:
            return False

    def borrar(self) -> str:
        from Banco import Banco
        for c in Banco.getClientes():
            # En caso de que queramos borrar, necesitamos quitar la tarjeta de todos los clientes que la tienen
            c.getTarjetasDebito().remove(self)
        return "La tarjeta de crédito #{} será borrada, ya que tiene demasiadas transacciones rechazadas".format(
            self.noTarjeta
        )

    def deshacerTransaccion(self, cantidad: float, t: Tarjeta) -> None:
        from TarjetaCredito import TarjetaCredito
        if self.saldo >= cantidad and isinstance(t, TarjetaDebito):
            # Deshacer una transacción desde una tarjeta de débito
            self.saldo -= int(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            t.setSaldo(t.getSaldo() + cantidad)
        elif (
                self.saldo >= cantidad
                and isinstance(t, TarjetaCredito)
                and t.getEspacio() >= cantidad
        ):
            # Deshacer una transacción desde una tarjeta de crédito
            self.saldo -= int(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            t.setCredito(cantidad)

    def tieneSaldo(self) -> bool:
        """
        Verifica si la tarjeta de débito tiene saldo disponible.

        Returns:
            bool: True si la tarjeta tiene saldo disponible, False en caso contrario.
        """
        return self.saldo != 0.0

    def puedeTransferir(self, monto: float) -> bool:
        """
        Verifica si la tarjeta de débito puede realizar una transferencia de la cantidad especificada.

        Args:
            monto (float): Cantidad a transferir.

        Returns:
            bool: True si la tarjeta puede realizar la transferencia, False en caso contrario.
        """
        return monto <= self.saldo

    def sacarDinero(self, monto: float) -> None:
        """
        Retira dinero de la tarjeta de débito.

        Args:
            monto (float): Cantidad a retirar.
        """
        if self.saldo >= monto:
            self.saldo -= monto

    def introducirDinero(self, monto: float) -> None:
        """
        Deposita dinero en la tarjeta de débito.

        Args:
            monto (float): Cantidad a depositar.
        """
        self.saldo += monto

    def setSaldo(self, monto):
        """
        Establece el saldo de la tarjeta de débito.

        Args:
            monto (float): Saldo a establecer.
        """
        self.saldo = monto

    def getSaldo(self):
        """
        Obtiene el saldo actual de la tarjeta de débito.

        Returns:
            float: Saldo actual de la tarjeta de débito.
        """
        return self.saldo

    def getDivisa(self):
        return super().getDivisa()
