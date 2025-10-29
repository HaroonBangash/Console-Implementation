export default function AccessibilityStatement() {
  return (
    <main className="mx-auto flex min-h-screen w-full max-w-3xl flex-col gap-6 px-6 py-16">
      <h1 className="text-3xl font-semibold">Accessibility at SkillForge</h1>
      <p className="text-sm text-muted-foreground">
        We are committed to meeting WCAG 2.2 AA guidelines. Keyboard navigation, screen reader support,
        and high-contrast themes are available across the platform.
      </p>
      <ul className="list-disc space-y-2 pl-6 text-sm text-muted-foreground">
        <li>Use the skip link at the top of each page to jump to main content.</li>
        <li>Enable high contrast mode from the top navigation toggle.</li>
        <li>Contact accessibility@skillforge.example for tailored support.</li>
      </ul>
    </main>
  );
}
