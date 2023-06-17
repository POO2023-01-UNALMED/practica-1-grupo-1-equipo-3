from typing import List
from enum import Enum
from Canal import Canal
from Divisa import Divisa
from Cliente import Cliente
from Tarjeta import Tarjeta
from Transaccion import Transaccion

class Banco():
    serialVersionUID = 1
    clientes = []
    canales = []


    @staticmethod
    def ordenarCanalesPorImpuestos(canales: List[Canal]):
        canales.sort()
        return canales

    @staticmethod
    def getClientes():
        return Banco.clientes

    @staticmethod
    def setClientes(clientes) -> None:
        Banco.clientes = clientes

    @staticmethod
    def agregarCliente(cliente: Cliente) -> None:
        Banco.clientes.append(cliente)

    @staticmethod
    def getCanales():
        return Banco.canales

    @staticmethod
    def setCanales(canales) -> None:
        Banco.canales = canales

    @staticmethod
    def agregarCanal(canal: Canal) -> None:
        Banco.canales.append(canal)

    @staticmethod
    def calcularPuntaje(trans) -> int:
        puntaje = 0
        for t in trans:
            puntaje += 2 * t.getCantidad() * t.getDivisa().getValor()
        return puntaje

    @staticmethod
    def seleccionarDivisa(cliente: Cliente):
        retorno = []
        for d in Divisa.values():
            for t in cliente.getTarjetas():
                if t.getDivisa() == d:
                    retorno.append(d)
                    break
        return retorno

    @staticmethod
    def numeroExistente(num: int) -> bool:
        valor = False
        for t in Tarjeta.getTarjetas():
            if num == t.getNoTarjeta():
                valor = True
                break
        return valor

    @staticmethod
    def generarPeticion(transaccion: Transaccion, mensaje: str) -> None:
        transaccion.setRetornable(False)
        Transaccion(transaccion, mensaje)


    @staticmethod
    def encontrarCliente(nomCliente) -> Cliente:
        for c in Banco.clientes:
            if c.getNombre() == nomCliente:
                return c

    """
    @staticmethod
    def formatearNumero(numero: float) -> str:
        esLocale = Locale("es", "CO")
        df = DecimalFormat.getInstance(esLocale)
        df.applyPattern("#,##0.00")
        return df.format(numero)
    """