import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminContenidoEditComponent } from './admin-contenido-edit.component';

describe('AdminContenidoEditComponent', () => {
  let component: AdminContenidoEditComponent;
  let fixture: ComponentFixture<AdminContenidoEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminContenidoEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminContenidoEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
