export type Lab = {
  id: string;
  title: string;
  attempts: number;
  maxAttempts: number;
  checklist: string[];
  evidence: { id: string; label: string }[];
};

export const labs: Lab[] = [
  {
    id: "lab-observability",
    title: "Observability Runbook Lab",
    attempts: 1,
    maxAttempts: 3,
    checklist: [
      "Spin up telemetry pipeline",
      "Inject load scenario",
      "Capture dashboard snapshot",
      "Export incident notes"
    ],
    evidence: [
      { id: "evidence-1", label: "Log bundle" },
      { id: "evidence-2", label: "Metrics dashboard capture" }
    ]
  },
  {
    id: "lab-blue-team",
    title: "SIEM Correlation Lab",
    attempts: 0,
    maxAttempts: 2,
    checklist: [
      "Baseline network traffic",
      "Correlate suspicious alerts",
      "Document containment plan"
    ],
    evidence: []
  }
];
