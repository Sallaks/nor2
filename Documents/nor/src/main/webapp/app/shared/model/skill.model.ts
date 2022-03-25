import { ISeniorityLevel } from 'app/shared/model/seniority-level.model';

export interface ISkill {
  id?: number;
  name?: string;
  seniorityLevel?: ISeniorityLevel | null;
}

export const defaultValue: Readonly<ISkill> = {};
