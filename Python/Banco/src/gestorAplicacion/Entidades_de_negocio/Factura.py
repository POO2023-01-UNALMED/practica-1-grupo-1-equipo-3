from typing import List
from Cliente import Cliente
from tarjetas.Tarjeta import Tarjeta
from tarjetas.TarjetaDebito import TarjetaDebito
from Transaccion import Transaccion


class Factura:
    def __init__(self, cliente, total: float, transfeRestantes: int, tarjetaDestino):
        """
        Crea una instancia de Factura.

        Args:
            cliente (Cliente): Cliente asociado a la factura.
            total (float): Monto total de la factura.
            transfeRestantes (int): Número de transferencias restantes.
            tarjetaDestino (Tarjeta): Tarjeta de destino de la factura.
        """
        self.CLIENTE = cliente
        self.DIVISA = tarjetaDestino.getDivisa()
        self.TOTAL = total
        self.valorPagado = 0
        self.transfeRestantes = transfeRestantes
        self.TARJETADESTINO = tarjetaDestino
        self.facturaVencida = False
        self.facturaPagada = False
        cliente.agregarFactura(self)

    def getTransfeRestantes(self) -> int:
        """
        Obtiene el número de transferencias restantes de la factura.

        Returns:
            int: Número de transferencias restantes.
        """
        return self.transfeRestantes

    def isFacturaVencida(self) -> bool:
        """
        Verifica si la factura está vencida.

        Returns:
            bool: True si la factura está vencida, False en caso contrario.
        """
        return self.facturaVencida

    def isFacturaPagada(self) -> bool:
        """
        Verifica si la factura está pagada.

        Returns:
            bool: True si la factura está pagada, False en caso contrario.
        """
        return not self.facturaPagada

    def getPendiente(self) -> float:
        """
        Obtiene el monto pendiente de la factura.

        Returns:
            float: Monto pendiente.
        """
        return self.TOTAL - self.valorPagado

    def getTOTAL(self) -> float:
        """
        Obtiene el monto total de la factura.

        Returns:
            float: Monto total de la factura.
        """
        return self.TOTAL

    def getDIVISA(self):
        """
        Obtiene la divisa de la factura.

        Returns:
            Divisa: Divisa de la factura.
        """
        return self.DIVISA

    def __str__(self):
        """
        Devuelve una representación en forma de cadena de la factura.

        Returns:
            str: Representación en forma de cadena de la factura.
        """
        if self.facturaPagada and not self.facturaVencida:
            retorno = "Factura pagada ANTES de vencer\nTarjeta objetivo: " + str(
                self.TARJETADESTINO.getNoTarjeta()) + "\n"
        elif self.facturaPagada:
            retorno = "Factura pagada DESPUÉS de vencer\nTarjeta objetivo: " + str(
                self.TARJETADESTINO.getNoTarjeta()) + "\n"
        elif self.facturaVencida:
            retorno = "Factura vencida por pagar.\nTarjeta objetivo: " + str(
                self.TARJETADESTINO.getNoTarjeta()) + " faltan " + str(
                self.TOTAL - self.valorPagado) + " " + self.DIVISA.name() + " por pagar\n"
        else:
            retorno = "Factura no vencida por pagar.\nTarjeta objetivo: {} faltan {} {} por pagar en {} " \
                      "transferencias\n".format(
                self.TARJETADESTINO.getNoTarjeta(), self.TOTAL - self.valorPagado, self.DIVISA.name,
                self.transfeRestantes)
        return retorno

    def generarTransaccion(self, monto: float, tarjetaOrigen) -> 'Transaccion':
        """
        Genera una transacción asociada a la factura.

        Args:
            monto (float): Monto de la transacción.
            tarjetaOrigen (Tarjeta): Tarjeta de origen de la transacción.

        Returns:
            Transaccion: La transacción generada.
        """
        validez = tarjetaOrigen.puedeTransferir(monto) and tarjetaOrigen.getDivisa() == self.TARJETADESTINO.getDivisa()
        if validez:
            self.transfeRestantes -= 1
            self.valorPagado += monto
            if self.valorPagado >= self.TOTAL:
                self.facturaPagada = True
        return Transaccion(self.CLIENTE, None, tarjetaOrigen, self.TARJETADESTINO, monto, not validez, self)

    @staticmethod
    def modificarPuntaje(tarjetasBloqueadas: List[Tarjeta], tarjetasActivas: List[Tarjeta], cliente: Cliente,
                         puntaje: int) -> int:
        """
        Modifica el puntaje del cliente en función de las tarjetas, facturas y transacciones.

        Args:
            tarjetasBloqueadas (List[Tarjeta]): Lista de tarjetas bloqueadas.
            tarjetasActivas (List[Tarjeta]): Lista de tarjetas activas.
            cliente (Cliente): Cliente asociado.
            puntaje (int): Puntaje actual.

        Returns:
            int: Puntaje modificado.
        """
        for f in cliente.getFacturas():
            if f.facturaVencida:
                puntaje -= 50
            if f.facturaPagada and not f.facturaVencida:
                puntaje += 0.5 * f.TOTAL * f.DIVISA.getValor()
        for t in tarjetasActivas:
            if isinstance(t, TarjetaDebito):
                puntaje += int(0.1 * (t.getSaldo() * t.getDivisa().getValor()))
        if len(tarjetasActivas) + len(tarjetasBloqueadas) != 0:
            puntaje -= 100 * len(tarjetasBloqueadas) / (len(tarjetasActivas) + len(tarjetasBloqueadas))
        return puntaje
