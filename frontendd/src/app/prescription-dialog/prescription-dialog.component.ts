import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-prescription-dialog',
  standalone:false,
  templateUrl: './prescription-dialog.component.html',
  styleUrls: ['./prescription-dialog.component.css']
})
export class PrescriptionDialogComponent {
  diagnosis: string = '';
  treatment: string = '';

  constructor(
    public dialogRef: MatDialogRef<PrescriptionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onSave(): void {
    if (!this.diagnosis || !this.treatment) {
      alert('Please fill in all fields.');
      return;
    }

    const prescription = {
      patientId: this.data.patientId,
      diagnosis: this.diagnosis,
      treatment: this.treatment,
      dateOfVisit: this.data.date
    };

    this.dialogRef.close(prescription); // Pass the prescription data back to the parent component
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}