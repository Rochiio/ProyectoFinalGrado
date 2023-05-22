import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarRef, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  constructor(public snackBar: MatSnackBar) { }
  private horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  private verticalPosition: MatSnackBarVerticalPosition = 'top';

  showAdoption(message: string): void {
    this.snackBar.open('🐱🐱 ' + message, '', {
      verticalPosition: this.verticalPosition,
      horizontalPosition: this.horizontalPosition
    });
  }

  showError(message: string): void {
    this.snackBar.open('❌ ' + message, '', {
      verticalPosition: this.verticalPosition,
      horizontalPosition: this.horizontalPosition
    });
  }

  showCorrect(message: string): void {
    this.snackBar.open('✅ ' + message, '', {
      verticalPosition: this.verticalPosition,
      horizontalPosition: this.horizontalPosition
    });
  }

}


