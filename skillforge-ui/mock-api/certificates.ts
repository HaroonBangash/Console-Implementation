export type Certificate = {
  id: string;
  program: string;
  issuer: string;
  issueDate: string;
  status: "Issued" | "Pending";
  verifyUrl: string;
};

export const certificates: Certificate[] = [
  {
    id: "cert-ai",
    program: "AI-enabled Customer Success",
    issuer: "SkillForge Institute",
    issueDate: "2024-04-30",
    status: "Issued",
    verifyUrl: "/verify?hash=abc123"
  }
];
