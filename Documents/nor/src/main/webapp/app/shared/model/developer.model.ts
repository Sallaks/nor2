import { ISkill } from 'app/shared/model/skill.model';

export interface IDeveloper {
  id?: number;
  name?: string | null;
  age?: number | null;
  skills?: ISkill[] | null;
}

export const defaultValue: Readonly<IDeveloper> = {};
