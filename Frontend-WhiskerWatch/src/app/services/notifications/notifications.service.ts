import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarRef, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  constructor(public snackBar: MatSnackBar) { }
  private horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  private verticalPosition: MatSnackBarVerticalPosition = 'top';

  /**
   * Mostrar snackbar de recogida de colonia
   * @param message mensaje a mostrar
   */
  showAdoption(message: string): void {
    this.snackBar.open('🐱🐱 ' + message, '', {
      verticalPosition: this.verticalPosition,
      horizontalPosition: this.horizontalPosition
    });
  }

  /**
   * Mostrar snackbar de accion error
   * @param message mensaje a mostrar
   */
  showError(message: string): void {
    this.snackBar.open('❌ ' + message, '', {
      verticalPosition: this.verticalPosition,
      horizontalPosition: this.horizontalPosition
    });
  }

  /**
   * Mostrar snackbar de accion correcta
   * @param message mensaje a mostrar
   */
  showCorrect(message: string): void {
    this.snackBar.open('✅ ' + message, '', {
      verticalPosition: this.verticalPosition,
      horizontalPosition: this.horizontalPosition
    });
  }

}


