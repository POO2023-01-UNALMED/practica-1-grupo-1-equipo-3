from abc import ABC, abstractmethod


class BorrarTarjeta(ABC):
    erroresMax = 3  # Número máximo de errores permitidos

    @abstractmethod
    def borrar(self):
        """
        Método abstracto que no tiene implementación concreta.
        Debe ser implementado por las subclases y define la lógica para borrar una tarjeta.
        """
        pass

    @abstractmethod
    def setErroresActuales(self, erroresActuales):
        """
        Método abstracto que recibe un parámetro "erroresActuales" y no tiene implementación concreta.
        Debe ser implementado por las subclases y se utiliza para establecer el número actual de errores.
        """
        pass

    @abstractmethod
    def tarjetaABorrar(self):
        """
        Método abstracto que no tiene implementación concreta.
        Debe ser implementado por las subclases y define la lógica para seleccionar la tarjeta a borrar.
        """
        pass

    @abstractmethod
    def anadirError(self):
        """
        Método abstracto que no tiene implementación concreta.
        Debe ser implementado por las subclases y define la lógica para añadir un error al contador de errores.
        """
        pass

