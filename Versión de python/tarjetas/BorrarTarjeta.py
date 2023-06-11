from abc import ABC, abstractmethod

class BorrarTarjeta(ABC):
    erroresMax = 3

    @abstractmethod
    def borrar(self):
        pass

    @abstractmethod
    def setErroresActuales(self, erroresActuales):
        pass

    @abstractmethod
    def tarjetaABorrar(self):
        pass

    @abstractmethod
    def anadirError(self):
        pass