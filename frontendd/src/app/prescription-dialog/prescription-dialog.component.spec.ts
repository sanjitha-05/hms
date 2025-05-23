import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrescriptionDialogComponent } from './prescription-dialog.component';

describe('PrescriptionDialogComponent', () => {
  let component: PrescriptionDialogComponent;
  let fixture: ComponentFixture<PrescriptionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PrescriptionDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrescriptionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
