from typing import List

from Banco.src.gestorAplicacion.infraestructura.Canal import Canal
from Banco.src.gestorAplicacion.Entidades_de_negocio.Cliente import Cliente
from Banco.src.gestorAplicacion.Entidades_de_negocio.Divisa import Divisa
from Banco.src.gestorAplicacion.tarjetas.Tarjeta import Tarjeta
from Banco.src.gestorAplicacion.Entidades_de_negocio.Transaccion import Transaccion


class Banco:
    clientes = []  # Lista para almacenar los objetos de la clase "Cliente"
    canales = []  # Lista para almacenar los objetos de la clase "Canal"

    @staticmethod
    def ordenarCanalesPorImpuestos(canales: List[Canal]):
        """
        Método estático que recibe una lista de canales y los ordena en función del impuesto de cada canal.
        El canal con el impuesto más bajo estará al principio de la lista.
        Retorna la lista ordenada de canales.
        """
        canales.sort(key=lambda x: x.getImpuesto())
        return canales

    @staticmethod
    def getClientes():
        """
        Método estático que retorna la lista de clientes almacenada en el atributo de clase "clientes".
        """
        return Banco.clientes

    @staticmethod
    def setClientes(clientes) -> None:
        """
        Método estático que recibe una lista de clientes y la asigna al atributo de clase "clientes".
        """
        Banco.clientes = clientes

    @staticmethod
    def agregarCliente(cliente: Cliente) -> None:
        """
        Método estático que recibe un objeto de la clase "Cliente" y lo agrega a la lista de clientes.
        """
        Banco.clientes.append(cliente)

    @staticmethod
    def getCanales():
        """
        Método estático que retorna la lista de canales almacenada en el atributo de clase "canales".
        """
        return Banco.canales

    @staticmethod
    def setCanales(canales) -> None:
        """
        Método estático que recibe una lista de canales y la asigna al atributo de clase "canales".
        """
        Banco.canales = canales

    @staticmethod
    def agregarCanal(canal: Canal) -> None:
        """
        Método estático que recibe un objeto de la clase "Canal" y lo agrega a la lista de canales.
        """
        Banco.canales.append(canal)

    @staticmethod
    def calcularPuntaje(trans) -> int:
        """
        Método estático que recibe una lista de transacciones y calcula un puntaje basado en la cantidad y la divisa de cada transacción.
        Retorna el puntaje calculado.
        """
        puntaje = 0
        for t in trans:
            puntaje += 2 * t.getCantidad() * t.getDivisa().getValor()
        return puntaje

    @staticmethod
    def seleccionarDivisa(cliente: Cliente):
        """
        Método estático que recibe un objeto de la clase "Cliente" y devuelve una lista de divisas asociadas a las tarjetas del cliente.
        """
        retorno = []
        for d in Divisa:
            for t in cliente.getTarjetas():
                if t.getDivisa() == d:
                    retorno.append(d)
                    break
        return retorno

    @staticmethod
    def numeroExistente(num: int) -> bool:
        """
        Método estático que verifica si existe una tarjeta con un número dado en la lista de tarjetas.
        Retorna True si el número existe, de lo contrario, retorna False.
        """
        valor = False
        for t in Tarjeta.getTarjetas():
            if num == t.getNoTarjeta():
                valor = True
                break
        return valor

    @staticmethod
    def generarPeticion(transaccion: Transaccion, mensaje: str) -> None:
        """
        Método estático que genera una nueva transacción con la información proporcionada y la marca como no retornable.
        La transacción se crea con el estado "pendiente".
        """
        transaccion.setRetornable(False)
        Transaccion(cliente_origen=transaccion.cliente_origen, cliente_objetivo=transaccion.cliente_objetivo,
                    tarjeta_origen=transaccion.tarjeta_origen, tarjeta_objetivo=transaccion.tarjeta_objetivo,
                    cantidad=transaccion.cantidad, mensaje=mensaje, retornable=False, pendiente=True)

    @staticmethod
    def encontrarCliente(nomCliente) -> Cliente:
        """
        Método estático que busca y retorna un objeto de la clase "Cliente" por su nombre.
        Retorna el cliente encontrado o None si no se encuentra ningún cliente con el nombre proporcionado.
        """
        for c in Banco.clientes:
            if c.getNombre() == nomCliente:
                return c

    @staticmethod
    def encontrarCanal(canal) -> Canal:
        """
        Método estático que busca y retorna un objeto de la clase "Canal" por su nombre.
        Retorna el canal encontrado o None si no se encuentra ningún canal con el nombre proporcionado.
        """
        for c in Banco.canales:
            if c.__str__() == canal:
                return c

    def __str__(self):
        return f"Banco\nNúmero de clientes: {len(self.clientes)}\nNúmero de canales: {len(self.canales)}"