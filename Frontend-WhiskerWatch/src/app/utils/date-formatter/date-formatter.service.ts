import { DatePipe, formatDate } from '@angular/common';
import { Inject, Injectable, LOCALE_ID } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateFormatterService {

  constructor(
    private datePipe: DatePipe,
    @Inject(LOCALE_ID) private locale: string
  ) { }

  /**
   * Formatear fecha.
   * @param date fecha a transformar.
   * @returns fecha formateada
   */
  public transformDate(date: Date): string {
    return formatDate(date, 'dd-MM-yyyy', this.locale);
  }

}
